package com.elmaddinasger.ecommerce

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.elmaddinasger.ecommerce.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgbtnHome.isClickable = false

        binding.imgbtnProfile.setOnClickListener {
            navigateToProfile()
        }
        binding.imgbtnShare.setOnClickListener {
            navigateToShare()
        }

        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val isFisrtTime = sharedPreferences.getBoolean("isFirstTime",true)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if(isFisrtTime) {
            navController.navigate(R.id.onboardingFragment)
        } else {
            if (isLoggedIn) {
                navController.navigate(R.id.homeFragment)
            } else {
                navController.navigate(R.id.signInFragment)
            }
        }

    }

      
    private fun navigateToProfile () {
        binding.navHostFragment.findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        binding.imgbtnHome.isClickable = true
        binding.imgbtnHome.background = ContextCompat.getDrawable(this,R.drawable.inactive_navigate_button_background)
        binding.imgbtnProfile.background = ContextCompat.getDrawable(this,R.drawable.active_navigate_button_background)
        binding.imgbtnProfile.isClickable = false
    }

    private fun navigateToShare () {
        binding.navHostFragment.findNavController().navigate(R.id.action_homeFragment_to_shareFragment)
        binding.imgbtnHome.isClickable = true
        binding.imgbtnHome.background = ContextCompat.getDrawable(this,R.drawable.inactive_navigate_button_background)
        binding.imgbtnShare.background = ContextCompat.getDrawable(this,R.drawable.active_navigate_button_background)
        binding.imgbtnShare.isClickable = false
    }

}