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
import com.example.uas.databinding.FragmentAddAdminBinding
import com.example.uas.databinding.FragmentAddStationBinding
import com.example.uas.databinding.FragmentEditAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddStationFragment : Fragment() {

    private var _binding: FragmentAddStationBinding? = null
    private val firestore = FirebaseFirestore.getInstance()
    private val stationcollectionRef = firestore.collection("station")

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddStationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnAdd.setOnClickListener {
                val station = station.text.toString()

                val newStation = Station(itstation = station)
                addStation(newStation)
                findNavController().navigateUp()
            }
        }
    }

    private fun addStation(station: Station) {
        stationcollectionRef.add(station).addOnSuccessListener { documentReference ->
            val createdStationId = documentReference.id
            station.id = createdStationId

            stationcollectionRef.document(createdStationId).set(station)
        }
    }

}