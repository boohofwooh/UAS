package com.example.uas.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas.TripAdapter
import com.example.uas.data.Trip
import com.example.uas.databinding.FragmentDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var tripAdapter: TripAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val tripcollectionRef = firestore.collection("trip")
    private val tripListLiveData: MutableLiveData<List<Trip>> by lazy {
        MutableLiveData<List<Trip>>()
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tripAdapter = TripAdapter(
            onClickTrip = { }, onDelete = { }, onEdit = {}
        )
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvPerjalanan.layoutManager = LinearLayoutManager(requireContext())
            rvPerjalanan.adapter = tripAdapter

            observeTrips()
            getAllTrips()

            val action = DashboardFragmentDirections.actionNavigationDashboardToAddTripFragment()
            btnAddPerjalanan.setOnClickListener {
                findNavController().navigate(action)
            }
        }
    }

    private fun getAllTrips() {
        observeTripsChanges()
    }

    private fun observeTripsChanges() {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE)
        val sharedUsername = sharedPreferences.getString("username", "")

        val username = sharedUsername.toString()

        tripcollectionRef.whereEqualTo("username", username).addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("DashboardFragment", "Error listening for trip changes:", error)
                return@addSnapshotListener
            }

            val trips = snapshots?.toObjects(Trip::class.java)
            if (trips != null) {
                tripListLiveData.postValue(trips)
            }
        }

    }

    private fun observeTrips() {
        tripListLiveData.observe(viewLifecycleOwner) { trips ->
            tripAdapter.submitList(trips)
        }
    }
}
