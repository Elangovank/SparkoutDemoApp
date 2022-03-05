package com.elango.demoapp.listener

import com.elango.demoapp.model.MapModel

interface CommonListener {

    fun click(value: MapModel)
    fun click(value: Int)
}