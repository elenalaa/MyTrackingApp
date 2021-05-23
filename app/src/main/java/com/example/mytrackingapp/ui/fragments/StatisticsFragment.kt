package com.example.mytrackingapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mytrackingapp.R
import com.example.mytrackingapp.ui.viewmodels.StatisticsViewModel

//@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private val viewModel: StatisticsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_statistics, container, false)

    companion object {
        fun newInstance(): StatisticsFragment = StatisticsFragment()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}
