package com.example.vrchat_ui_test

//TROY: OSC class to be used with ChatActivity. This is necessary as this OSC message has more than 1 argument.
class OSCChat(ipAddress:String, portAddress:Int, messageAddress:String, parameters:List<Any>) : Thread(){
    private val ip = ipAddress
    private val port = portAddress
    private val message = messageAddress
    private val arguments = parameters

    override fun run(){
        val sender = OSCSender(ip, port)
        sender.send(message, arguments)
    }
}