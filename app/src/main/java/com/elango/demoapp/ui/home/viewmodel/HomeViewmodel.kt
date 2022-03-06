package com.elango.demoapp.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import com.elango.demoapp.model.MapDataDAO
import com.elango.demoapp.model.MapModel
import com.elango.demoapp.repository.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewmodel @Inject constructor(private val mapRepo: MapRepository) : ViewModel() {

    fun insertMapData(data: MapModel) {
        mapRepo.insert(data)
    }

}