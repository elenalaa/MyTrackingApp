package com.example.mytrackingapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mytrackingapp.R
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.mytrackingapp.service.TrackingService
import com.example.mytrackingapp.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


//@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking), OnMapReadyCallback {
    //val CITY: String = "helsinki,fi"
    //val API: String = "ea24a5db5d70a7fa2d93a248d0fd9029"
    private lateinit var map: GoogleMap
    private val isTracking = false
    //private var trackPoints = mutableListOf<>()


    private val btnToggleTrack: Button? = view?.findViewById<Button>(R.id.btnToggleTrack)
    private var mapView: SupportMapFragment?=null

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? =
        inflater.inflate(R.layout.fragment_tracking, container, false)

    companion object {
        fun newInstance(): TrackingFragment = TrackingFragment()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //mapView.onCreate(savedInstanceState)
        btnToggleTrack?.setOnClickListener {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
        mapView = fragmentManager?.findFragmentById(R.id.mapView) as SupportMapFragment?
        Log.println(Log.INFO,"gde", "tut2")
        mapView?.getMapAsync (this)
        Log.println(Log.INFO,"gde", mapView.toString())
    }




    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.println(Log.INFO,"gde", "tut")
        if(googleMap != null ){
            map= googleMap
        }
    }
}






