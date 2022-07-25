package com.example.helpingminds.Model

import java.util.*

class Reminder {
    var reminderId:Int? = null
    var eventId:Int = 0
    var priority:Int = 0
    var repeat:Int = 0

    constructor(id:Int?, event: Int, pty: Int, rpt: Int){
        this.reminderId = id
        this.eventId = event
        this.priority = pty
        this.repeat = rpt
    }
}