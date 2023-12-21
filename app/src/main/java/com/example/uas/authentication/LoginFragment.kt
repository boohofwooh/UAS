package com.example.uas.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.uas.admin.AdminActivity
import com.example.uas.user.UserActivity
import com.example.uas.data.Users
import com.example.uas.databinding.FragmentLoginBinding
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()
    private val userCollectionRef = firestore.collection("users")

    private lateinit var binding: FragmentLoginBinding
    private val PREFS_NAME = "MyPrefsFile"
    private val USER_ROLE_KEY = "userRole"
    private val IS_LOGGED_IN_KEY = "isLoggedIn"
    private val USERNAME_KEY = "username"
    private val EMAIL_KEY = "email"
    private val PHONE_KEY = "phone"
    private val PASSWORD_KEY = "password"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val username = binding.username
        val password = binding.password
        val button = binding.loginBtn
        val register = binding.txtRegister

        button.setOnClickListener {
            val stringUsername = username.text.toString()
            val stringPassword = password.text.toString()

            fetchUserData(stringUsername, stringPassword)
        }

        register.setOnClickListener {
            val viewPager = (activity as? MainActivity)?.getViewPager()
            viewPager?.currentItem = 0
        }

        if (isLoggedIn()) {
            val userRole = getUserRole()
            handleUserRole(userRole)
        }

        return view
    }

    private fun fetchUserData(username: String, password: String) {
        userCollectionRef.whereEqualTo("username", username)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.documents[0].toObject(Users::class.java)
                    if (user != null) {
                        saveUserRole(user.role, user.username, user.email, user.phone, user.password)
                        saveLoginStatus(true)
                        handleUserRole(user.role)
                    } else {
                        Log.d("LoginFragment", "User data is null")
                    }
                } else {
                    Log.d("LoginFragment", "Invalid username or password")
                    Toast.makeText(
                        requireContext(),
                        "Invalid username or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("LoginFragment", "Error getting user data", exception)
            }
    }

    private fun saveUserRole(role: String, username: String, email: String, phone: String, password: String) {
        val sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(USER_ROLE_KEY, role)
        editor.putString(USERNAME_KEY, username)
        editor.putString(EMAIL_KEY, email)
        editor.putString(PHONE_KEY, phone)
        editor.putString(PASSWORD_KEY, password)
        editor.apply()
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(IS_LOGGED_IN_KEY, isLoggedIn)
        editor.apply()
    }

    private fun isLoggedIn(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(IS_LOGGED_IN_KEY, false)
    }

    private fun getUserRole(): String {
        val sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(USER_ROLE_KEY, "") ?: ""
    }

    private fun handleUserRole(role: String) {
        when (role) {
            "admin" -> {
                val intent = Intent(requireContext(), AdminActivity::class.java)
                startActivity(intent)
            }
            "user" -> {
                val intent = Intent(requireContext(), UserActivity::class.java)
                startActivity(intent)
            }
            else -> {
                Log.d("LoginFragment", "Unknown role")
            }
        }
        // Finish the current activity to prevent going back to login
        requireActivity().finish()
    }
}
