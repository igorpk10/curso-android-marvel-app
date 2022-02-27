package com.example.marvelapp

import androidx.appcompat.app.AppCompatActivity
import android.os.*
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.marvelapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
    }

    private var navController: NavController? = null
    private var appBarConfiguration: AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf())
    }
}