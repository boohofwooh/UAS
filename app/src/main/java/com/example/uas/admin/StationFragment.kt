package com.example.uas.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas.AdminAdapter
import com.example.uas.R
import com.example.uas.StationAdapter
import com.example.uas.data.Station
import com.example.uas.data.Users
import com.example.uas.databinding.FragmentStationBinding
import com.google.firebase.firestore.FirebaseFirestore

class StationFragment : Fragment() {

    private var _binding: FragmentStationBinding? = null
    private lateinit var stationAdapter: StationAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val stationcollectionRef = firestore.collection("station")
    private val stationListLiveData : MutableLiveData<List<Station>> by lazy {
        MutableLiveData<List<Station>>()
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val action = StationFragmentDirections.actionNavigationStationToAddStationFragment()
            btnAdd.setOnClickListener {
                findNavController().navigate(action)
            }
            rvStation.layoutManager = LinearLayoutManager(requireContext())
            stationAdapter = StationAdapter(
                onDelete = { station ->
                    deleteStation(station) },
                onEdit = { station ->
                    val bundle = Bundle()
                    bundle.putString("id", station.id)
                    bundle.putString("station", station.itstation)

                    findNavController().navigate(
                        R.id.action_navigation_station_to_editStationFragment, bundle)
                }
            )
            rvStation.adapter = stationAdapter

            observeStations()
            getAllStations()

        }
    }

    private fun getAllStations(){
        observeStationsChanges();
    }

    private fun observeStationsChanges(){
        stationcollectionRef.addSnapshotListener{ snapshots, error ->
            if (error != null){
                Log.d("MainActivity","Error listening for admin changes:", error)
            }
            val stations = snapshots?.toObjects(Station::class.java)
            if (stations != null) {
                stationListLiveData.postValue(stations)
            }
        }
    }

    private fun observeStations(){
        stationListLiveData.observe(requireActivity()){
                station ->
            stationAdapter.submitList(station)
        }
    }

    private fun deleteStation(station: Station){
        if (station.id.isEmpty()){
            Log.d("MainActivity","error delete item: station Id is empty!")
            return
        }
        stationcollectionRef.document(station.id).delete().addOnFailureListener {
            Log.d("Main activity", "Error deleting station", it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}