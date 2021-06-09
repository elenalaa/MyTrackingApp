package com.example.mytrackingapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytrackingapp.adapter.TrackAdapter
import com.example.mytrackingapp.databinding.FragmentTrackBinding
import com.example.mytrackingapp.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.reflect.KProperty

//import kotlinx.android.synthetic.main.fragment_track.*


//@AndroidEntryPoint
class TrackFragment : Fragment() {

    private var _binding: FragmentTrackBinding? = null

    private val binding get() = _binding!!
    private lateinit var trackAdapter: TrackAdapter

    @SuppressLint("StringFormatInvalid")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackBinding.inflate(inflater, container, false)


        //setupRecyclerView()

        /*viewModel.tracks.observe(viewLifecycleOwner, Observer {
            trackAdapter.submitList(it)
        })*/
    return binding.root

}
    companion object {
        fun newInstance(): TrackFragment = TrackFragment()

        //private val args: TrackFragmentArgs by navArgs()
        //private val viewModel: MainViewModel by viewModels()
        private lateinit var trackAdapter: TrackAdapter
    }

   /* private fun setupRecyclerView() = rvTracks.apply {
        trackAdapter = TrackAdapter()
        adapter = trackAdapter
        layoutManager = LinearLayoutManager(requireContext())*/



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}






