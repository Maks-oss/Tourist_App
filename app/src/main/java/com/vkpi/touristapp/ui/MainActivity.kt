package com.vkpi.touristapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vkpi.touristapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        setUpBottomNav(navController)
    }
    private fun setUpBottomNav(navController: NavController) {
        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)
    }
    fun hideBottomNavigationBar(){
        bottomNav.visibility= View.GONE
    }
    fun showBottomNavigationBar(){
        bottomNav.visibility= View.VISIBLE
    }
}