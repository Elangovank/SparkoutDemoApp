package com.suguna.broilerdoc.api

/**
 * Created by Mathan on 19/06/20.
 **/

interface ResponseListener {
    /**
     * @param r - The model class that is passed on the parser
     */
    fun onResponse(r: Responses?)
}

