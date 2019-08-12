package com.project.siapp

class Message(val id: String, val text: String, val fromId: String, val toId: String, val timeStamp: Long){
    constructor(): this("","","","", 0)
}