package com.example.mytrackingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.mytrackingapp.databinding.FragmentTrackBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//import kotlinx.android.synthetic.main.fragment_track.*


//@AndroidEntryPoint
class TrackFragment : BottomSheetDialogFragment () {

    private val args: TrackFragmentArgs by navArgs()

    private var _binding: FragmentTrackBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("StringFormatInvalid")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackBinding.inflate(inflater, container, false)
        binding.distanceValueTV.text = getString(R.string._0_0km, args.trackResult.distance)
        binding.timerValueTextView5.text = args.trackResult.time

        binding.saveButton.setOnClickListener {
            saveTrackResult()

    }

    return binding.root

}

    private fun saveTrackResult() {
        val saveIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT,"Track was ${args.trackResult.distance}km in ${args.trackResult.time}" )

        }
        startActivity(saveIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

    /*private fun requestPermissions() {
        if(TrackingPermissions.checkLocationPermissions(requireContext())) {
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions.",
                REQUEST_CODE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

     override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

     override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }*/



