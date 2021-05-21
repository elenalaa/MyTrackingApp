package com.example.mytrackingapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mytrackingapp.R
import com.example.mytrackingapp.ui.viewmodels.MainViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking){

    private val viewModel: MainViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_tracking, container, false)

    companion object {
        fun newInstance(): TrackingFragment = TrackingFragment()
    }
}
