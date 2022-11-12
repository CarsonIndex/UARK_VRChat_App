package com.example.vrchat_ui_test

import android.util.Log

//TROY: This is the OSC class to be used with only MainActivity. Use OSCAdvanced for other Activities.
class OSCModel(ipAddress:String, tag:String, pressed:Boolean) : Thread(){
    private val address:String
    private val message:String
    private val state:Boolean

    init{
        address = ipAddress
        message = tag
        state = pressed
    }

    override fun run(){ //TROY: The program doesn't like network requests on the main thread, so OSCModel extends the Thread class.
        val sender = OSCSender(address)
        Log.d(message, state.toString())
        when(message){
            "moveForward"->sendForward(sender,state)
            "moveBackward"->sendBackward(sender,state)
            "moveLeft"->sendLeft(sender,state)
            "moveRight"->sendRight(sender,state)
            "moveJump"->sendJump(sender,state)
            "singleJump"->sendJumpSingle(sender)
            "lookLeft"->sendLookLeft(sender,state)
            "lookRight"->sendLookRight(sender,state)
            "voice"->sendVoiceToggle(sender)
            "voicePush"->sendVoice(sender, state)
            "sprint"->sendSprint(sender, state)
            "voiceOn"->sendVoice(sender,true)
            "voiceOff"->sendVoice(sender,false)
        }
    }

    private fun sendForward(sender:OSCSender, value:Boolean){
        if(value) sender.send("/input/MoveForward", 1)
        else sender.send("/input/MoveForward", 0)
    }

    private fun sendBackward(sender:OSCSender, value:Boolean){
        if(value) sender.send("/input/MoveBackward", 1)
        else sender.send("/input/MoveBackward", 0)
    }

    private fun sendLeft(sender:OSCSender, value:Boolean){
        if(value) sender.send("/input/MoveLeft", 1)
        else sender.send("/input/MoveLeft", 0)
    }

    private fun sendRight(sender:OSCSender, value:Boolean){
        if(value) sender.send("/input/MoveRight", 1)
        else sender.send("/input/MoveRight", 0)
    }

    private fun sendLookLeft(sender:OSCSender, value:Boolean){
        if(value) sender.send("/input/LookLeft", 1)
        else sender.send("/input/LookLeft", 0)
    }

    private fun sendLookRight(sender:OSCSender, value:Boolean){
        if(value) sender.send("/input/LookRight", 1)
        else sender.send("/input/LookRight", 0)
    }

    private fun sendJump(sender:OSCSender, value:Boolean){
        if(value) sender.send("/input/Jump", 1)
        else sender.send("/input/Jump", 0)
    }

    //TROY: Sleep is needed to have delay between 1 and 0.
    private fun sendJumpSingle(sender: OSCSender){
        sender.send("/input/Jump", 1)
        sleep(100)
        sender.send("/input/Jump", 0)
    }

    private fun sendVoice(sender:OSCSender, value:Boolean){ //TROY: When in push-to-talk mode, the Mic is on when 1 and off when 0.
        if(value) sender.send("/input/Voice", 1)
        else sender.send("/input/Voice", 0)
    }

    //TROY: Sleep is needed to have delay between 1 and 0.
    private fun sendVoiceToggle(sender: OSCSender){ //TROY: When in normal mode, the Mic toggles when going from 0 to 1.
        sender.send("/input/Voice", 1)
        sleep(100)
        sender.send("/input/Voice", 0)
    }

    private fun sendSprint(sender:OSCSender, value:Boolean){
        if(value) sender.send("/input/Run", 1)
        else sender.send("/input/Run", 0)
    }

}