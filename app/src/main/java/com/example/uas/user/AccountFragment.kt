package com.example.uas.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.uas.authentication.MainActivity
import com.example.uas.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE)

        val username = sharedPref.getString("username", "")
        val email = sharedPref.getString("email", "")
        val phone = sharedPref.getString("phone", "")
        val password = sharedPref.getString("password", "")

        binding.apply {
            txtUsername.text = username
            txtEmail.text = email
            txtPhone.text = phone
            txtPassword.text = password
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }
    private fun logout() {
        val sharedPref = requireActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(requireContext(), MainActivity::class.java)

        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
