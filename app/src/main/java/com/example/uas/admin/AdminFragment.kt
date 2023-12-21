package com.example.uas.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas.AdminAdapter
import com.example.uas.R
import com.example.uas.data.Users
import com.example.uas.databinding.FragmentAdminBinding
import com.google.firebase.firestore.FirebaseFirestore


class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private lateinit var adminAdapter: AdminAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val admincollectionRef = firestore.collection("users")
    private val adminListLiveData : MutableLiveData<List<Users>> by lazy {
        MutableLiveData<List<Users>>()
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val action = AdminFragmentDirections.actionNavigationAdminToAddAdminFragment()
            btnAdd.setOnClickListener {
                findNavController().navigate(action)
            }
            rvAdmin.layoutManager = LinearLayoutManager(requireContext())
            adminAdapter = AdminAdapter(
                onDelete = { admin ->
                    deleteAdmin(admin) },
                onEdit = { admin ->
                    val bundle = Bundle()
                    bundle.putString("id", admin.id)
                    bundle.putString("username", admin.username)
                    bundle.putString("email", admin.email)
                    bundle.putString("phone", admin.phone)
                    bundle.putString("password", admin.password)
                    bundle.putString("role", admin.role)

                    findNavController().navigate(
                        R.id.action_navigation_admin_to_editAdminFragment2, bundle)
                }
            )
            rvAdmin.adapter = adminAdapter

            observeAdmins()
            getAllAdmins()

        }
    }

    private fun getAllAdmins(){
        observeAdminsChanges();
    }

    private fun observeAdminsChanges(){
        admincollectionRef.addSnapshotListener{ snapshots, error ->
            if (error != null){
                Log.d("MainActivity","Error listening for admin changes:", error)
            }
            val admins = snapshots?.toObjects(Users::class.java)
            if (admins != null) {
                val adminUsers = admins.filter { it.role.equals("admin", ignoreCase = true) }
                adminListLiveData.postValue(adminUsers)
            }
        }
    }

    private fun observeAdmins(){
        adminListLiveData.observe(requireActivity()){
                admins ->
            adminAdapter.submitList(admins)
        }
    }

    private fun deleteAdmin(users: Users){
        if (users.id.isEmpty()){
            Log.d("MainActivity","error delete item: admin Id is empty!")
            return
        }
        admincollectionRef.document(users.id).delete().addOnFailureListener {
            Log.d("Main activity", "Error deleting admin", it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}