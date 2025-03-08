package com.elmaddinasger.ecommerce

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.elmaddinasger.ecommerce.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgbtnHome.isClickable = false

        binding.imgbtnProfile.setOnClickListener {
            navigateToProfile()
        }
    }

    private fun navigateToProfile () {
        binding.fragmentContainerView.findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        binding.imgbtnHome.isClickable = true
        binding.imgbtnHome.background = ContextCompat.getDrawable(this,R.drawable.inactive_navigate_button_background)
        binding.imgbtnProfile.background = ContextCompat.getDrawable(this,R.drawable.active_navigate_button_background)
        binding.imgbtnProfile.isClickable = false
    }

}