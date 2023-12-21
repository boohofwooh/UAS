package com.example.uas.user

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.uas.R
import com.example.uas.Trips
import com.example.uas.TripsRoomDatabase
import com.example.uas.WtripDao
import com.example.uas.data.Trip
import com.example.uas.databinding.FragmentAddTripBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class AddTripFragment : Fragment() {
    private var _binding: FragmentAddTripBinding? = null
    private lateinit var mTripDao: WtripDao
    private val binding get() = _binding!!
    private val channelId = "TEST_NOTIF"
    private val notifId = 90

    private val firestore = FirebaseFirestore.getInstance()
    private val stasiunCollectionRef = firestore.collection("station")
    private val tripcollectionRef = firestore.collection("trip")
    private lateinit var executorService: ExecutorService



    val spinnerKelasKereta = arrayOf("Ekonomi", "Eksekutif", "Bisnis")
    val spinnerKursiKereta = arrayOf("A1", "A2", "A3", "A4", "A5","B1", "B2", "B3", "B4", "B5", "C1", "C2", "C3", "C4", "C5")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTripBinding.inflate(inflater, container, false)
        executorService = Executors.newSingleThreadExecutor()

        val root: View = binding.root
        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        actionBar?.title = "Tambah Perjalanan"
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE)
        val sharedusername = sharedPreferences.getString("username","")
        val notifManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        with(binding) {
            executorService = Executors.newSingleThreadExecutor()
            mTripDao = TripsRoomDatabase.getInstance(requireContext()).tripsDao()
            val listPaket = mutableListOf<String>()
            val cal = Calendar.getInstance()

            calendarViewBrgkt.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val selectedCal = Calendar.getInstance()
                selectedCal.set(year, month, dayOfMonth)
                if (selectedCal.before(cal)) {
                    Toast.makeText(
                        requireContext(),
                        "Tanggal keberangkatan tidak boleh lebih awal dari tanggal saat ini",
                        Toast.LENGTH_SHORT
                    ).show()
                    calendarViewBrgkt.date = cal.timeInMillis
                } else {
                    val selectedDateBrgkt = "$dayOfMonth-${month + 1}-$year"
                    tanggalBrgkt.text = selectedDateBrgkt

                    calendarViewKembali.minDate = selectedCal.timeInMillis
                }
            }

            calendarViewKembali.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val selectedCal = Calendar.getInstance()
                selectedCal.set(year, month, dayOfMonth)
                if (selectedCal.before(cal) || tanggalBrgkt == null) {
                    Toast.makeText(
                        requireContext(),
                        "Pilih tanggal keberangkatan terlebih dahulu",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Reset to the minimum date allowed
                    calendarViewKembali.date = cal.timeInMillis
                } else {
                    val selectedDateKembali = "$dayOfMonth-${month + 1}-$year"
                    tanggalKembali.text = selectedDateKembali
                }
            }

            stasiunCollectionRef.get()
                .addOnSuccessListener { documents ->
                    val stasiunList = mutableListOf<String>()

                    for (document in documents) {
                        val stasiun = document.getString("itstation")
                        stasiun?.let {
                            stasiunList.add(it)
                        }
                    }

                    val adapterStasiunAwal = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        stasiunList
                    )
                    adapterStasiunAwal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerStasiunAwal.adapter = adapterStasiunAwal

                    val adapterStasiunTujuan = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        stasiunList.toMutableList()
                    )
                    adapterStasiunTujuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerStasiunTujuan.adapter = adapterStasiunTujuan

                    spinnerStasiunAwal.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parentView: AdapterView<*>,
                            selectedItemView: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedStasiunAwal = stasiunList[position]
                            val stasiunListTujuan = stasiunList.toMutableList()
                            stasiunListTujuan.remove(selectedStasiunAwal)
                            adapterStasiunTujuan.clear()
                            adapterStasiunTujuan.addAll(stasiunListTujuan)
                            adapterStasiunTujuan.notifyDataSetChanged()
                        }

                        override fun onNothingSelected(parentView: AdapterView<*>) {

                        }
                    })

                }
                .addOnFailureListener { exception ->

                }

            val adapterKelas = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
            spinnerKelasKereta)
            adapterKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerKelas.adapter = adapterKelas

            val adapterKursi = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, spinnerKursiKereta
            )
            adapterKursi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerKursi.adapter = adapterKursi

            spinnerKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    updateHargaBasedOnKelas()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            tambahanAnak.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    updateHarga(50000)
                } else {
                    updateHarga(-50000)
                }
            }

            konsum.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    updateHarga(25000)
                } else {
                    updateHarga(-25000)
                }
            }

            internet.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    updateHarga(5000)
                } else {
                    updateHarga(-5000)
                }
            }

            bagasi.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    updateHarga(10000)
                } else {
                    updateHarga(-10000)
                }
            }

            porter.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    updateHarga(20000)
                } else {
                    updateHarga(-20000)
                }
            }

            pijat.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    updateHarga(15000)
                } else {
                    updateHarga(-15000)
                }
            }

            audio.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    updateHarga(35000)
                } else {
                    updateHarga(-35000)
                }
            }

            updateHargaBasedOnKelas()

            btnPesan.setOnClickListener {
                if (tambahanAnak.isChecked){
                    listPaket.add(tambahanAnak.text.toString())
                }
                if (konsum.isChecked){
                    listPaket.add(konsum.text.toString())
                }
                if (internet.isChecked){
                    listPaket.add(internet.text.toString())
                }
                if (bagasi.isChecked){
                    listPaket.add(bagasi.text.toString())
                }
                if (porter.isChecked){
                    listPaket.add(porter.text.toString())
                }
                if (pijat.isChecked){
                    listPaket.add(pijat.text.toString())
                }
                if (audio.isChecked){
                    listPaket.add(audio.text.toString())
                }

                val tgl_brgkt = tanggalBrgkt.text.toString()
                val tgl_kembali = tanggalKembali.text.toString()
                val stasiun_awal = spinnerStasiunAwal.selectedItem.toString()
                val stasiun_tujuan = spinnerStasiunTujuan.selectedItem.toString()
                val kursi = spinnerKursi.selectedItem.toString()
                val kelas = spinnerKelas.selectedItem.toString()
                val harga = harga.text.toString()
                val username = sharedusername.toString()

                val newTrip = Trip(tgl_brgkt = tgl_brgkt, tgl_kembali = tgl_kembali, st_awal = stasiun_awal,
                    st_tujuan = stasiun_tujuan, kursi = kursi, kelas = kelas, harga = harga, list_paket = listPaket, username = username)
                addTrip(newTrip)
                val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    PendingIntent.FLAG_MUTABLE
                }
                else{
                    0
                }
                val intent = requireActivity().packageManager.getLaunchIntentForPackage(requireActivity().packageName)
                val pendingIntent = PendingIntent.getActivity(
                    requireContext(),0,intent,flag
                )

                val builder = NotificationCompat.Builder(
                    requireContext(),
                    channelId
                ).setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle("My Train")
                    .setContentText("Rencana perjalanan berhasil ditambahkan! Ingatlah jadwal perjalanan Anda")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    val notifChannel = NotificationChannel(
                        channelId, "My Train",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    with(notifManager) {
                        createNotificationChannel(notifChannel)
                        notify(notifId, builder.build())
                    }
                } else {
                    notifManager.notify(notifId, builder.build())
                }
                findNavController().navigateUp()
            }

            btnAddToWishlist.setOnClickListener {
                val tgl_brgkt = tanggalBrgkt.text.toString()
                val tgl_kembali = tanggalKembali.text.toString()
                val stasiun_awal = spinnerStasiunAwal.selectedItem.toString()
                val stasiun_tujuan = spinnerStasiunTujuan.selectedItem.toString()

                insert(Trips(tgl_brgkt = tgl_brgkt, tgl_kembali = tgl_kembali, st_awal = stasiun_awal, st_tujuan = stasiun_tujuan))

                findNavController().navigateUp()
            }

        }
    }

    private fun updateHarga(hargaTambahan: Int) {
        val currentHarga = binding.harga.text.toString().toInt()
        val newHarga = currentHarga + hargaTambahan
        binding.harga.text = newHarga.toString()
    }

    private fun updateHargaBasedOnKelas() {
        val selectedKelas = binding.spinnerKelas.selectedItem as? String
        var hargaAwal = 0

        selectedKelas?.let {
            when (it) {
                "Ekonomi" -> hargaAwal = 75000
                "Eksekutif" -> hargaAwal = 150000
                "Bisnis" -> hargaAwal = 175000
            }
            binding.harga.text = hargaAwal.toString()
        } ?: run {
            binding.harga.text = "0"
        }
    }

    private fun addTrip(trip: Trip){
        tripcollectionRef.add(trip).addOnSuccessListener { documentReference ->
            val createdTripId = documentReference.id
            trip.id = createdTripId

            documentReference.set(trip).addOnFailureListener {
                Log.d("MainActivity", "Error updating class id: ", it)
            }
        }.addOnFailureListener {
            Log.d("MainActivity", "Error adding class id: ", it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun insert(trips: Trips){
        executorService.execute {
            mTripDao.insert(trips)
        }
    }


}
