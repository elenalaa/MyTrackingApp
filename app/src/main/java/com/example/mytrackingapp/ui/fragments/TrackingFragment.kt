package com.example.mytrackingapp.ui.fragments

import android.content.Intent
import android.os.Bundle
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
import com.google.android.gms.maps.MapView


//@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {
    //val CITY: String = "helsinki,fi"
    //val API: String = "ea24a5db5d70a7fa2d93a248d0fd9029"
    private var map: GoogleMap? = null
    private val btnToggleTrack: Button? = getView()?.findViewById(R.id.btnToggleTrack)
    private val mapView: MapView? = getView()?.findViewById(R.id.mapView) as MapView

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
        //val mapView: MapView = getView()?.findViewById(R.id.mapView) as MapView

        mapView?.onCreate(savedInstanceState)
        //val btnToggleTrack: Button? = getView()?.findViewById(R.id.btnToggleTrack)
        btnToggleTrack?.setOnClickListener {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
        mapView!!.getMapAsync {
            map = it
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
}






