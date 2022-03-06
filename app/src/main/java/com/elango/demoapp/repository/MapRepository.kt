package com.elango.demoapp.repository

import com.elango.demoapp.model.MapDataDAO
import com.elango.demoapp.model.MapModel

class MapRepository(val mapDao: MapDataDAO) {

    fun insert(data: MapModel)  {
       return mapDao.insert(data)
    }
}