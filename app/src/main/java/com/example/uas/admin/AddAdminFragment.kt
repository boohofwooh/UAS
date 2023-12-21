package com.example.uas.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.uas.R
import com.example.uas.data.Users
import com.example.uas.databinding.FragmentAddAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddAdminFragment : Fragment() {

    private var _binding: FragmentAddAdminBinding? = null

    private val binding get() = _binding!!

    private val firestore = FirebaseFirestore.getInstance()
    private val admincollectionRef = firestore.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnAdd.setOnClickListener {
                val username = username.text.toString()
                val email = email.text.toString()
                val phone= phone.text.toString()
                val password = password.text.toString()
                val role = "admin"

                val newAdmin = Users(username = username, email = email, phone = phone, password = password, role = role)
                addAdmin(newAdmin)
                findNavController().navigateUp()
            }
        }
    }

    private fun addAdmin(users: Users) {
        admincollectionRef.add(users).addOnSuccessListener { documentReference ->
            val createdAdminId = documentReference.id
            users.id = createdAdminId

            admincollectionRef.document(createdAdminId).set(users)
        }
    }

}