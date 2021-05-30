package com.example.mytrackingapp.ui.fragments

import android.content.Intent
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mytrackingapp.R
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_PAUSE_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.mytrackingapp.service.TrackingService
import com.example.mytrackingapp.ui.viewmodels.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker


//@AndroidEntryPoint
@Suppress("DEPRECATION")
class TrackingFragment : Fragment(R.layout.fragment_tracking) {
    //val CITY: String = "helsinki,fi"
    private val viewModel: MainViewModel by viewModels()
    private var map: GoogleMap? = null
    private var mapView: SupportMapFragment? = null

    internal lateinit var mLastLocation: Location
    internal lateinit var mLocationResult: LocationRequest
    internal lateinit var mLocationCallback: LocationCallback
    internal var mCurrLocationMarker: Marker? = null
    //internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    internal var mFusedLocationClient: FusedLocationProviderClient? = null

    private var isTracking = false
    private var trackPoints = mutableListOf<Point>()


    private val btnToggleTrack: Button? = view?.findViewById<Button>(R.id.btnToggleTrack)


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
        mapView?.onCreate(savedInstanceState)
        btnToggleTrack?.setOnClickListener {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            toggleTrack()
        }
        mapView = fragmentManager?.findFragmentById(R.id.mapView) as SupportMapFragment?
        Log.println(Log.INFO,"gde", "tut2")
        mapView?.getMapAsync {
            map = it
        }
        Log.println(Log.INFO, "gde", mapView.toString())
        subscribeToObservers()
    }

    //When isTracking TrackingService will be updated by Observer
    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.trackPoints.observe(viewLifecycleOwner, Observer {
            trackPoints = trackPoints
            //moveCameraToUser()

        })
    }

    private fun toggleTrack() {
        if (isTracking) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }


    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            btnToggleTrack!!.text = "Start"
            btnToggleTrack.visibility = View.VISIBLE
        } else {
            btnToggleTrack!!.text = "Stop"
            btnToggleTrack.visibility = View.GONE
        }
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


    /*override fun onMapReady(googleMap: GoogleMap) {
        Log.println(Log.INFO,"gde", "tut")

        map= googleMap as GoogleMap
      val sydney = LatLng(-34.0, 151.0)

        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("You are here!"))
        googleMap.moveCamera(newLatLng(sydney))
        Log.println(Log.INFO, "gde camera", googleMap.cameraPosition.toString())

    }*/

}











