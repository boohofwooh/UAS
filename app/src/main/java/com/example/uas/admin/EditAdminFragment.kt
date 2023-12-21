package com.example.uas.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.uas.data.Users
import com.example.uas.databinding.FragmentEditAdminBinding
import com.google.firebase.firestore.FirebaseFirestore


class EditAdminFragment : Fragment() {
    private var _binding: FragmentEditAdminBinding? = null
    private val firestore = FirebaseFirestore.getInstance()
    private val admincollectionRef = firestore.collection("users")
    private var updateId = ""

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val id = arguments?.getString("id")
            val username = arguments?.getString("username")
            val email = arguments?.getString("email")
            val phone = arguments?.getString("phone")
            val password = arguments?.getString("password")
            val role = arguments?.getString("role")

            if (id != null) {
                updateId = id
            }
            binding.apply {
                edtUsername.setText(username)
                edtEmail.setText(email)
                edtPhone.setText(phone)
                edtPassword.setText(password)

                btnSave.setOnClickListener {
                    updateAdmin(Users(
                        id = updateId,
                        username = edtUsername.text.toString(),
                        email = edtEmail.text.toString(),
                        phone = edtPhone.text.toString(),
                        password = edtPassword.text.toString(),
                        role = role.toString()
                    ))

                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun updateAdmin(users: Users){
        users.id = updateId
        admincollectionRef.document(updateId).set(users)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}