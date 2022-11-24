package com.example.vrchat_ui_test

import android.util.Log
import com.illposed.osc.OSCMessage
import com.illposed.osc.OSCPortOut
import java.net.InetAddress

class OSCSender(ipAddress:String, portAddress:Int = 9000) {
    private var port:OSCPortOut

    init{
        val socket:InetAddress = InetAddress.getByName(ipAddress)
        if(MainActivity.isNetworkAvailable()) port = OSCPortOut(socket, portAddress) //TROY: VRChat input port is 9000 by default.
        else port = OSCPortOut(InetAddress.getByName("127.0.0.1"), portAddress) //TROY: If no network is detected, the the localhost is used to prevent a crash.
    }

    fun send(address:String, argument:Any){  //TROY: This method is to be used to send a message to the VRChat program.
        port.send(OSCMessage(address, listOf(argument)))
    }

    fun send(address:String, argument: List<Any>){  //TROY: This method is to be used to send to ChatBox.
        port.send(OSCMessage(address, argument))
    }

    fun send(address:String, argument:Int){  //TROY: A variation of send that accepts integers.
        port.send(OSCMessage(address, listOf(argument)))
    }

    fun send(address:String, argument:String){  //TROY: A variation of send that accepts strings.
        port.send(OSCMessage(address, listOf(argument)))
    }

    fun send(address:String, argument:Float){  //TROY: A variation of send that accepts floats.
        port.send(OSCMessage(address, listOf(argument)))
    }

    fun send(address:String, argument:Boolean){  //TROY: A variation of send that accepts booleans.
        port.send(OSCMessage(address, listOf(argument)))
    }

}