package com.example.uas.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.uas.R
import com.example.uas.data.Station
import com.example.uas.data.Users
import com.example.uas.databinding.FragmentEditAdminBinding
import com.example.uas.databinding.FragmentEditStationBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditStationFragment : Fragment() {
    private var _binding: FragmentEditStationBinding? = null
    private val firestore = FirebaseFirestore.getInstance()
    private val stationcollectionRef = firestore.collection("station")
    private var updateId = ""

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditStationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val id = arguments?.getString("id")
            val station = arguments?.getString("station")

            if (id != null) {
                updateId = id
            }
            binding.apply {
                edtStasiun.setText(station)

                btnSave.setOnClickListener {
                    updateStation(
                        Station(
                        id = updateId,
                        itstation = edtStasiun.text.toString(),
                    )
                    )

                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun updateStation(stations: Station){
        stations.id = updateId
        stationcollectionRef.document(updateId).set(stations)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}