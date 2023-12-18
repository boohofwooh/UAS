// WishlistFragment.kt

package com.example.uas.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas.databinding.FragmentWishlistBinding
import com.example.uas.TripDao
import com.example.uas.wTrip
import com.example.uas.wTripAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var wTripDao: TripDao
    private lateinit var executorService: ExecutorService
    private lateinit var wTripAdapter: wTripAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        executorService = Executors.newSingleThreadExecutor()
        val db = TripRoomDatabase.getInstance(requireContext())
        wTripDao = db.tripDao()!!

        with(binding) {
            rvWishlist.layoutManager = LinearLayoutManager(requireContext())
            rvWishlist.adapter = wTripAdapter
            wTripAdapter = wTripAdapter(
                onClickClass = {},
                onDelete = { wTrip -> delete(wTrip) },
                onEdit = { wTrip -> /* Handle edit action */ }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getAllTrips()
    }

    private fun getAllTrips() {
        wTripDao.getAllClasses().observe(viewLifecycleOwner, Observer { tripList ->
            wTripAdapter.submitList(tripList)
        })
    }

    private fun delete(wTrip: wTrip) {
        executorService.execute {
            wTripDao.delete(wTrip)
        }
    }
}
