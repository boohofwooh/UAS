package com.example.uas.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.uas.admin.AdminActivity
import com.example.uas.TabAdapter
import com.example.uas.user.UserActivity
import com.example.uas.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var viewPager2: ViewPager2

    // SharedPreferences key for storing the user's role and login status
    private val PREFS_NAME = "MyPrefsFile"
    private val USER_ROLE_KEY = "userRole"
    private val IS_LOGGED_IN_KEY = "isLoggedIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            viewPager2 = viewPager

            viewPager.adapter = TabAdapter(this@MainActivity)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Register"
                    1 -> "Login"
                    else -> ""
                }
            }.attach()
        }

        // Check if the user is already logged in
        if (isLoggedIn()) {
            val userRole = getUserRole()
            handleUserRole(userRole)
        }
    }

    fun getViewPager(): ViewPager2 {
        return viewPager2
    }

    private fun isLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPref.getBoolean(IS_LOGGED_IN_KEY, false)
    }

    private fun getUserRole(): String {
        val sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPref.getString(USER_ROLE_KEY, "") ?: ""
    }

    private fun handleUserRole(role: String) {
        when (role) {
            "admin" -> {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            }
            "user" -> {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
            else -> {
                // Handle other cases if needed
            }
        }
        // Finish the current activity to prevent going back to login
        finish()
    }
}
