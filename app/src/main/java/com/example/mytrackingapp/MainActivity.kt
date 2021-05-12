package com.example.mytrackingapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var startLocation : Location

    private lateinit var map : GoogleMap

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if((Build.VERSION.SDK_INT >=23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                startLocation = task.result!!

                if (map != null) {
                    map.addMarker(MarkerOptions().position(LatLng(task.result!!.latitude, task.result!!.longitude)).title("You Are Here!"))

                } else {

                }
            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onLocationChanged(p0: Location) {
        //Display location
        findViewById<TextView>(R.id.latitude_text).text = getString(R.string.latitude_string, p0.latitude.toString())
        findViewById<TextView>(R.id.longitude_text).text = getString(R.string.longitude_string, p0.longitude.toString())

        //Display Speed
        findViewById<TextView>(R.id.speed_text).text = getString(R.string.speed_string, p0.speed.toString())
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.apply {
            map = googleMap
        }
    }
}