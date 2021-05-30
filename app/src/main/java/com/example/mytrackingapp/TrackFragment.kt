package com.example.mytrackingapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

//import kotlinx.android.synthetic.main.fragment_track.*


//@AndroidEntryPoint
class TrackFragment : Fragment (R.layout.fragment_track) {

    //private val viewModel: MainViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val butt = getView()?.findViewById<FloatingActionButton>(R.id.button_fab)

        /*butt?.setOnClickListener() {
            (context as MainActivity).changeFragment(TrackingFragment.newInstance())
            //findNavController().navigate(R.id.action_trackFragment_to_trackingFragment)
        }*/
    }
}

   /* override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_track, container, false)

    companion object {
        fun newInstance(): TrackFragment = TrackFragment()
    }


    private fun requestPermissions() {
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
    }
}*/


