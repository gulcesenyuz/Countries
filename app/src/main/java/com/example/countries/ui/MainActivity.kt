package com.example.countries.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment


import com.example.countries.R
import com.example.countries.databinding.ActivityMainBinding
import com.example.countries.ui.fragment.HomeFragment
import com.example.countries.ui.fragment.SavedCountriesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide();


        loadFragment(HomeFragment())
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.destination_home -> {
                    loadFragment(HomeFragment())
                }
                R.id.destination_saved -> {
                    loadFragment(SavedCountriesFragment())
                }

                else -> {}
            }
            true

        }

    }


    private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
        fragmentTransaction.commit()
    }

}


