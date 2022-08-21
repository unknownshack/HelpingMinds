package com.example.helpingminds.Model

import java.util.*

class Reminder {
    var reminderId: Int? = null
    var eventId: Int? = 0
    var priority: Int? = 0
    var reminderDate: String = "01/01/2010T00:00:00"
    var repeat: Int? = 0
    var numOfTimeRepeat: Int? = 0
    var uuid: String? = ""
    var completed: Boolean? = false
    var userId: Int? = null
    var actualRepeat: Boolean? = null
    var purpose: String? = null
    var isonoroff: Boolean? = null
    var nonEventNote: String? = null

    constructor(id: Int?, event: Int?, pty: Int, rpt: Int) {
        this.reminderId = id
        this.eventId = event
        this.priority = pty
        this.repeat = rpt
    }
}