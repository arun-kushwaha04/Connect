package com.example.connect.models

class message(
    val id: String?,
    var content:String,
    val fromId:String,
    val toId: String,
    val timestamp: Long
              ){
    constructor():this("","","","",-1)
}
