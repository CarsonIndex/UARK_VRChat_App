package com.example.vrchat_ui_test

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class SettingActivity : AppCompatActivity() {
    private var ip:String = "127.0.0.1"
    private var checkPersist:String = "0"
    private lateinit var PTT_Box: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        PTT_Box = findViewById(R.id.PTT_Box)

        ip = intent.getStringExtra("ipAddress")!!
        checkPersist = intent.getStringExtra("checkBool")!!

        if (checkPersist == "1")
            PTT_Box.isChecked = true
        else
            PTT_Box.isChecked = false

        updateIP()
    }

    fun returnIP(view: View){
        onBackPressed()
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
    override fun onBackPressed(){
        val returnIntent = Intent()

        val persistent = applicationContext.getSharedPreferences("IP Storage", MODE_PRIVATE)
        val persistent_box = applicationContext.getSharedPreferences("Checkbox Storage", MODE_PRIVATE)
        val editor = persistent.edit()
        val editor_box = persistent_box.edit()
        editor.putString("ipAddress", ip)
        editor.apply()

        returnIntent.putExtra("ipAddress", ip)
        if (PTT_Box.isChecked) {
            editor_box.putString("pushToTalk", "true")
            returnIntent.putExtra("pushToTalk", "true")
        }
        else {
            editor_box.putString("pushToTalk", "false")
            returnIntent.putExtra("pushToTalk", "false")
        }
        editor_box.apply()
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}