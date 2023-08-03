package com.example.vrchat_ui_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class ChatActivity : AppCompatActivity() {
    private var ip:String = "127.0.0.1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        ip = intent.getStringExtra("ipAddress")!!

        //TROY: This block of code initializes chatText with its previous value.
        val persistent = applicationContext.getSharedPreferences("IP Storage", MODE_PRIVATE)
        if(persistent.getString("messageChat", "") != null) findViewById<EditText>(R.id.chatText).setText(persistent.getString("messageChat", ""))
    }

    //TROY: This method is what sends the message to the VRChat chat box.
    fun attemptChat(view: View){
        val chatString = findViewById<EditText>(R.id.chatText).text.toString()

        //TROY: The "true" is what makes the text send through the chat box rather than just appearing in the box in VRChat.
        val senderChat = OSCChat(ip, 9000, "/chatbox/input", listOf(chatString, true))

        senderChat.start()
    }

    fun attemptYoutube(view: View){
        val linkString = findViewById<EditText>(R.id.chatText).text.toString()

        val senderLink = OSCAdvanced(ip, 9001, "/VRCRC/YouTubeURL", linkString)

        senderLink.start()
    }

    fun attemptNext(view: View){
        val senderNext = OSCAdvanced(ip, 9001, "/VRCRC/NextTrack", 1)

        senderNext.start()
    }

    fun attemptPrevious(view: View){
        val senderPrevious = OSCAdvanced(ip, 9001, "/VRCRC/PreviousTrack", 1)

        senderPrevious.start()
    }

    fun attemptPlayPause(view: View){
        val senderPlayPause = OSCAdvanced(ip, 9001, "/VRCRC/PlayPauseTrack", 1)

        senderPlayPause.start()
    }

    fun attemptRestart(view: View){
        val senderRestart = OSCAdvanced(ip, 9001, "/VRCRC/RestartTrack", 1)

        senderRestart.start()
    }

    fun returnChat(view: View){
        onBackPressed()
    }

    //TROY: Before returning to the MainActivity, the ChatActivity stores the message in IP Storage.
    override fun onBackPressed(){
        val persistent = applicationContext.getSharedPreferences("IP Storage", MODE_PRIVATE)
        val editor = persistent.edit()
        editor.putString("messageChat", findViewById<EditText>(R.id.chatText).text.toString())
        editor.apply()

        finish()
    }
}