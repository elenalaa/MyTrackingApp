package com.example.mytrackingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mytrackingapp.adapter.TrackAdapter
import com.example.mytrackingapp.database.Track
import com.example.mytrackingapp.database.TrackDao
import com.example.mytrackingapp.database.TrackingDataBase
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
import com.example.mytrackingapp.ui.fragments.MapSet
import com.example.mytrackingapp.ui.fragments.MapSet.cameraPosition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

//@AndroidEntryPoint
class MapTrackingFragment : Fragment(), OnMapReadyCallback,  GoogleMap.OnMyLocationButtonClickListener {

//  private val viewModel: MainViewModel by viewModels()
    private lateinit var trackAdapter: TrackAdapter

    private var _binding: FragmentMapTrackingBinding? = null
    private val binding get() = _binding!!
    private var db: TrackingDataBase? = null
    private var trackDao: TrackDao? = null
    private lateinit var map: GoogleMap

    private var startTime = 0L
    private var stopTime = 0L

    private var curTime = 0L

    private var locationList = mutableListOf<LatLng>()
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
            endTrackAndSaveToDb()
        }
        binding.restartButton.setOnClickListener {
            onRestartButtonClicked()
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root

    }

    companion object {
        fun newInstance(): MapTrackingFragment = newInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        observeTrackingService()
        //setupRecyclerView()

        /*viewModel.tracksSortedByDate.observe(viewLifecycleOwner, {
            trackAdapter.submitList(it)
        })*/
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


    private fun observeTrackingService() {
        TrackingService.locationList.observe(viewLifecycleOwner, {
            if (it != null) {
                locationList = it
                if (locationList.size > 1) {
                    binding.stopButton.enable()
                }
                Log.d("LocationList", locationList.toString())
                followUser()

            }
        })
        TrackingService.started.observe(viewLifecycleOwner, {
            started.value = it
            //sentActionCommandToService(ACTION_START_OR_RESUME_SERVICE)
        })
        TrackingService.startTime.observe(viewLifecycleOwner, {
            startTime = it
        })
        TrackingService.stopTime.observe(viewLifecycleOwner, {
            stopTime = it
            //sentActionCommandToService(ACTION_STOP_SERVICE)
            //displayTrackResult()
        })
    }

    private fun followUser() {
        if (locationList.isNotEmpty()) {
            map?.animateCamera((
                    CameraUpdateFactory.newCameraPosition(
                        cameraPosition(
                            locationList.last()
                        ))), 1000, null)
        }

    }

    private fun onStartButtonClicked() {
        if (hasBackgroundLocationPermission(requireContext())) {
            binding.startButton.disable()
            binding.startButton.hide()
            binding.stopButton.show()
            sentActionCommandToService(ACTION_START_OR_RESUME_SERVICE)

        } else {
            requestBackgroundLocationPermission(this)
        }
    }

    private fun onRestartButtonClicked() {
        mapRestart()
    }

    //Give latest location point and moved camera in this point
    @SuppressLint("MissingPermission")
    private fun mapRestart() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
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
    private fun sentActionCommandToService(action: String) {
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
        lifecycleScope.launch {
            delay(1500)
            binding.hintTextView.hide()
            binding.startButton.show()
        }
        return false
    }

    private fun stopTrack() {
        sentActionCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_mapTrackingFragment_to_trackFragment)
    }


    @SuppressLint("WrongConstant")
    private fun endTrackAndSaveToDb() {

        var distance = MapSet.calculateDistance(locationList)
        /*for(location in locationList) {
                //distance += MapSet.calculateDistance().toInt()
            }*/
        stopTime = System.currentTimeMillis()
        val timestamp = Calendar.getInstance().time
        val time = TimeUnit.MILLISECONDS.toSeconds(stopTime - startTime)
        Log.d("track start", startTime.toString())
        Log.d("track stop", stopTime.toString())
        Log.d("track time", time.toString())
        Log.d("track distance", distance.toString())
        Log.d("track time diff", (stopTime - startTime).toString())
        val avgSpeed = (distance / time)
        val track = Track(timestamp.time, avgSpeed, distance.toInt(), time)
        //viewModel.insertTrack(track)
        Log.d("track data", Date(timestamp.time).toString())
        Log.d("track data", track.toString())

        /*lifecycleScope.launch {

            trackDao = TrackingDataBase.getTrackingDatabase(requireContext())?.getTrackDao()
            with(trackDao) {
                this?.insertTrack(track)
            }*/
           /* with(trackDao) {
                Log.d("db", this?.getAllTracksSortedByDate().toString())
            }*/
        //}
                Snackbar.make(
                requireActivity().findViewById(R.id.mapTrackingFragment),
                "Track saved successfully",
                LENGTH_LONG
            ).show()
            stopTrack()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

