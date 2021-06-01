package com.example.mytrackingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mytrackingapp.databinding.FragmentMapTrackingBinding
import com.example.mytrackingapp.model.Track
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.mytrackingapp.moredbclasses.Constants.ACTION_STOP_SERVICE
import com.example.mytrackingapp.moredbclasses.Functions.disable
import com.example.mytrackingapp.moredbclasses.Functions.enable
import com.example.mytrackingapp.moredbclasses.Functions.hide
import com.example.mytrackingapp.moredbclasses.Functions.show
import com.example.mytrackingapp.moredbclasses.Permissions.hasBackgroundLocationPermission
import com.example.mytrackingapp.moredbclasses.Permissions.requestBackgroundLocationPermission
import com.example.mytrackingapp.service.TrackingService
import com.example.mytrackingapp.ui.fragments.MapSet.calculateDistance
import com.example.mytrackingapp.ui.fragments.MapSet.cameraPosition
import com.example.mytrackingapp.ui.fragments.MapSet.updatedTimeForm
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@AndroidEntryPoint
class MapTrackingFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentMapTrackingBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    private var startTime = 0L
    private var stopTime = 0L

    private var locationList= mutableListOf<LatLng>()
    val started = MutableLiveData(false)

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapTrackingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.tracking = this

        binding.startButton.setOnClickListener {
            onStartButtonClicked()
        }
        binding.stopButton.setOnClickListener {
            onStopButtonClicked()
        }
        binding.restartButton.setOnClickListener {
            onRestartButtonClicked()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
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
        TrackingService.locationList.observe(viewLifecycleOwner, {
            if (it != null) {
                locationList = it
                if(locationList.size > 1){
                    binding.stopButton.enable()
                }
                Log.d("LocationList", locationList.toString() )
                followUser()

            }
        })
        TrackingService.started.observe(viewLifecycleOwner,{
           started.value = it
        })
        TrackingService.startTime.observe(viewLifecycleOwner, {
            startTime = it
        })
        TrackingService.stopTime.observe(viewLifecycleOwner, {
            stopTime = it
            displayTrackResult()
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

    private fun onRestartButtonClicked() {
        mapRestart()
    }

    //Give latest location point and moved camera in this point
    @SuppressLint("MissingPermission")
    private fun mapRestart() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener{
            val latestLocationPoint = LatLng(
                it.result.latitude,
                it.result.longitude
            )
            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    cameraPosition(latestLocationPoint)
                )
            )
            locationList.clear()
            binding.restartButton.hide()
            binding.startButton.show()
        }
    }
    /*fun onFinish(){
        sentActionCommandToService(ACTION_STOP_SERVICE)
    }*/

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

    private fun displayTrackResult(){
        val result = Track(
            calculateDistance(locationList),
            updatedTimeForm(startTime, stopTime)
        )
        lifecycleScope.launch{
            delay(2000)
            val directions = MapTrackingFragmentDirections.actionMapTrackingFragmentToTrackFragment(result)
            findNavController().navigate(directions)
            binding.startButton.apply {
                hide()
                enable()
            }
            binding.stopButton.hide()
            binding.restartButton.show()

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