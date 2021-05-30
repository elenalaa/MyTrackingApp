package com.example.mytrackingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mytrackingapp.databinding.FragmentMapTrackingBinding
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_STOP_SERVICE
import com.example.mytrackingapp.moredbclasses.Functions.disable
import com.example.mytrackingapp.moredbclasses.Functions.enable
import com.example.mytrackingapp.moredbclasses.Functions.hide
import com.example.mytrackingapp.moredbclasses.Functions.show
import com.example.mytrackingapp.moredbclasses.Permissions.hasBackgroundLocationPermission
import com.example.mytrackingapp.moredbclasses.Permissions.requestBackgroundLocationPermission
import com.example.mytrackingapp.service.TrackingService
import com.example.mytrackingapp.service.TrackingService.Companion.startTime
import com.example.mytrackingapp.service.TrackingService.Companion.stopTime
import com.example.mytrackingapp.ui.viewmodels.MapSet.cameraPosition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MapTrackingFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentMapTrackingBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    private var locationList = mutableListOf<LatLng>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapTrackingBinding.inflate(inflater, container, false)

        binding.startButton.setOnClickListener {
            onStartButtonClicked()
        }
        binding.stopButton.setOnClickListener {
            onStopButtonClicked()
        }
        binding.restartButton.setOnClickListener {

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        map.isMyLocationEnabled = true
        map.setOnMyLocationButtonClickListener(this)
        map.uiSettings.apply {
            isZoomControlsEnabled = false
            isZoomGesturesEnabled = false
            isRotateGesturesEnabled = false
            isScrollGesturesEnabled = false
        }
        observeTrackingService()
    }

    private fun observeTrackingService(){
        TrackingService.trackPointsList.observe(viewLifecycleOwner, {
            if (it != null)
                locationList = it
            if (locationList.size > 1) {
                binding.stopButton.enable()
            }
            followUser()

        })
        startTime.observe(viewLifecycleOwner, {
            startTime
        })
        stopTime.observe(viewLifecycleOwner, {
            stopTime
         })
    }

    private fun followUser(){
        if(locationList.isNotEmpty()){
            map.animateCamera((
                    CameraUpdateFactory.newCameraPosition(
                        cameraPosition(
                            locationList.last()
                        ))), 1000, null)
        }

    }

    private fun onStartButtonClicked() {
        if(hasBackgroundLocationPermission(requireContext())) {
            binding.startButton.disable()
            binding.startButton.hide()
            binding.stopButton.show()
            sentActionCommandToService(ACTION_START_OR_RESUME_SERVICE)

        }else{
                requestBackgroundLocationPermission(this)
            }
        }

    private fun onStopButtonClicked() {
        stopForegroundServices()
        binding.stopButton.hide()
        binding.startButton.show()
    }

    private fun stopForegroundServices() {
        binding.startButton.disable()
        sentActionCommandToService(ACTION_STOP_SERVICE)
    }


    //Sent action to Service
    private fun sentActionCommandToService(action: String){
        Intent(
            requireContext(),
            TrackingService::class.java
        ).apply {
            this.action = action
            requireContext().startService(this)
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        binding.hintTextView.animate().alpha(0f).duration = 1000
        lifecycleScope.launch{
            delay(1500)
        binding.hintTextView.hide()
        binding.startButton.show()
    }
    return false
}

//Permission functions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        @Suppress("DEPRECATION")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionDenied(this, perms[0])){
            SettingsDialog.Builder(requireActivity()).build().show()
        } else{
            requestBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        onStartButtonClicked()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}