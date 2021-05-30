package com.example.mytrackingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ArFragment : Fragment() {
    private val TAG = "EEE"
    private val MIN_OPENGL_VERSION = 3.0
    private var ar: ArFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? =
        inflater.inflate(R.layout.fragment_ar, container, false)

    companion object {
        fun newInstance(): ArFragment = ArFragment()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ar = childFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment?

        /*ar.setOnTapArPlaneListener { hitResult: HitResult, plane: Image.Plane, motionEvent: MotionEvent ->
            val trackable = hitResult.trackable
            Log.d(TAG, "HIIIT  ${hitResult.trackable}")
            if (trackable is Image.Plane) {
                val anchor = hitResult.createAnchor()

                placeObject(ar!!, anchor, Uri.parse("file:///android_asset/kanto.gltf"))
            }
        }

    }
}*/

    }
}




