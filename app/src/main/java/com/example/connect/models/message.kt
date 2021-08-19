package com.example.connect.models

class message(
    private val id: String,
    val content:String,
    val fromId:String,
    val toId: String,
    val timestamp: Long
              ){
    constructor():this("","","","",-1)
}
