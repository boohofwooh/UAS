package com.example.uas.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.uas.data.Users
import com.example.uas.databinding.FragmentRegisterBinding
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()
    private val usercollectionRef = firestore.collection("users")

    private lateinit var binding: FragmentRegisterBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        val username = binding.username
        val email = binding.email
        val phone = binding.phone
        val password = binding.password
        val button = binding.registerBtn
        val login = binding.txtLogin

        button.setOnClickListener {
            val stringUsername = username.text.toString()
            val stringEmail = email.text.toString()
            val stringPhone = phone.text.toString()
            val stringPassword = password.text.toString()
            val role = "user"

            if (stringUsername.isNotEmpty() && stringPassword.isNotEmpty() && binding.checkBox.isChecked) {
                val newUser = Users(username = stringUsername, email = stringEmail, phone = stringPhone, password = stringPassword, role = role)
                addUser(newUser)
                val viewPager = (activity as? MainActivity)?.getViewPager()
                viewPager?.currentItem = 1

            } else {
                Toast.makeText(requireContext(), "Please fill in all forms and check box", Toast.LENGTH_SHORT).show()
            }
        }

        login.setOnClickListener {
            val viewPager = (activity as? MainActivity)?.getViewPager()
            viewPager?.currentItem = 1
        }



        return view
    }

    private fun addUser(users: Users) {
        usercollectionRef.add(users)
            .addOnSuccessListener { documentReference ->
                val createUserId = documentReference.id
                users.id = createUserId

                usercollectionRef.document(createUserId)
                    .set(users)
                    .addOnSuccessListener {
                        Log.d("RegisterFragment", "User added successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.w("RegisterFragment", "Error updating user document", e)
                        Toast.makeText(
                            requireContext(),
                            "Error updating user document",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .addOnFailureListener { e ->
                Log.w("RegisterFragment", "Error adding user to collection", e)
                Toast.makeText(
                    requireContext(),
                    "Error adding user to collection: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}
