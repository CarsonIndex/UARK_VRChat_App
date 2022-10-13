package com.example.vrchat_ui_test

//TROY: This method exists to be used with AdvancedActivity. It's for custom OSC signals and should NOT be used with MainActivity!
class OSCAdvanced(ipAddress:String, portAddress:Int, messageAddress:String, parameter:Any? = null) : Thread(){
    private val ip = ipAddress
    private val port = portAddress
    private val message = messageAddress
    private val argument = parameter

    override fun run(){
        val sender = OSCSender(ip, port)
        if(argument != null) sender.send(message, argument)
    }
}