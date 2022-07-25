package com.example.helpingminds.Model

import java.io.Serializable
import java.util.*

class Event: Serializable {
    var eventId:Int = -1
    var eventName:String = ""
    var eventDate: String = "7/23/2022"

    constructor(id:Int, name:String, date:String){
        this.eventId = id
        this.eventName = name
        this.eventDate = date
    }
}