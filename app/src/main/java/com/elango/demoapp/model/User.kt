package com.elango.demoapp.model


import com.suguna.broilerdoc.api.Responses

import java.io.Serializable
class User : Responses(), Serializable {

    var by: String? = null
    var score: String? = null
    var title: String? = null
    var type: String? = null
    var url: String? = null
    var text: String? = null


}