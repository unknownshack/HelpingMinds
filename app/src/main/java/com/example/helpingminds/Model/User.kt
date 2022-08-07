package com.example.helpingminds.Model

class User {
    var userId:Int = -1
    var userName:String = ""
    var userPassword:String = ""
    private var isAdmin = false

    constructor(id:Int, name:String, pass:String){
        this.userId = id
        this.userName = name
        this.userPassword = pass
    }

}