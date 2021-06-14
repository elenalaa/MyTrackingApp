package com.example.mytrackingapp


//import kotlinx.android.synthetic.main.activity_main.*

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mytrackingapp.moredbclasses.Permissions.hasLocationPermission
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    var selectedFragment: Fragment? = null

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        navController = findNavController(R.id.navHostFragment)

        bottomNavigation.setupWithNavController(navController)

        if (hasLocationPermission(this)) {
            navController.navigate(R.id.action_permissionFragment_to_mapTrackingFragment)
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.navHostFragment, fragment)
            .addToBackStack(null).commit()
    }
}











