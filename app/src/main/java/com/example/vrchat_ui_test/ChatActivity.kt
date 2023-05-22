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