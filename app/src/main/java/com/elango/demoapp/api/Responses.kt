package com.suguna.broilerdoc.api

import java.io.Serializable



open class Responses : Serializable {
    var responseStatus: Boolean = true
    var responseMessage: String? = null
    var requestType: Int? = null
}
