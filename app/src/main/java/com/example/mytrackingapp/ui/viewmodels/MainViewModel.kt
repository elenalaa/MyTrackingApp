package com.example.mytrackingapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mytrackingapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


//Collect Data from Repository and provide it for fragments
@HiltViewModel
class MainViewModel  @Inject constructor (
    val Repository: Repository) : ViewModel(){


    }
