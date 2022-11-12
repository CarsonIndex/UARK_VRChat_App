package com.example.vrchat_ui_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

class SettingActivity : AppCompatActivity() {
    private var ip:String = "127.0.0.1"
    private lateinit var PTT_Box: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        PTT_Box = findViewById(R.id.PTT_Box)

        ip = intent.getStringExtra("ipAddress")!!
        updateIP()
    }

    //TROY: Function that puts IP address from input text to variable.
    fun applyIP(view: View){
        val textField = findViewById<EditText>(R.id.ipEditAdvanced)
        if(textField.text.toString().matches(Patterns.IP_ADDRESS.toRegex())){
            ip = textField.text.toString()
            updateIP()
        }
    }

    //TROY: Function that updates ipView to reflect ip variable.
    private fun updateIP(){
        val ipView = findViewById<TextView>(R.id.ipView)
        ipView.text = ip
    }

    //TROY: Function that ends current activity to return updated ip to MainActivity
    fun returnIP(view: View){
        val returnIntent = Intent()

        val persistent = applicationContext.getSharedPreferences("IP Storage", MODE_PRIVATE)
        val editor = persistent.edit()
        editor.putString("ipAddress", ip)
        editor.apply()

        returnIntent.putExtra("ipAddress", ip)
        if (PTT_Box.isChecked) {
            returnIntent.putExtra("pushToTalk", "true")
        }
        else {
            returnIntent.putExtra("pushToTalk", "false")
        }
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}