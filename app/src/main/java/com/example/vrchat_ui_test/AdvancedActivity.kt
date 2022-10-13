package com.example.vrchat_ui_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.lang.NumberFormatException

class AdvancedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)

        //TROY: This block of code initializes the text edits with previous values.
        val persistent = applicationContext.getSharedPreferences("IP Storage", MODE_PRIVATE)
        if(persistent.getString("ipAdvanced", "") != null) findViewById<EditText>(R.id.ipEditAdvanced).setText(persistent.getString("ipAdvanced", ""))
        if(persistent.getString("portAdvanced", "") != null) findViewById<EditText>(R.id.portEdit).setText(persistent.getString("portAdvanced", ""))
        if(persistent.getString("messageAdvanced", "") != null) findViewById<EditText>(R.id.messageEdit).setText(persistent.getString("messageAdvanced", ""))
        if(persistent.getString("parameterAdvanced", "") != null) findViewById<EditText>(R.id.parameterEdit).setText(persistent.getString("parameterAdvanced", ""))
    }

    private fun addressIsValid() : Boolean{ //TROY: Method that checks that ip address is formatted correctly.
        return findViewById<EditText>(R.id.ipEditAdvanced).text.toString().matches(Patterns.IP_ADDRESS.toRegex())
    }

    private fun portIsValid() : Boolean{    //TROY: Method that checks that port is formatted correctly.
        val tempInt = Integer.valueOf(findViewById<EditText>(R.id.portEdit).text.toString())
        if((tempInt != null) && (tempInt < 10000) && (tempInt > -1)) return true
        return false
    }

    fun attemptSend(view: View){    //TROY: Method that sends OSC signal.
        val tempStatus = findViewById<TextView>(R.id.statusView)
        tempStatus.text = ""
        if(!addressIsValid()){
            tempStatus.text = "Invalid IP Address!"
            return
        } else if(!portIsValid()){
            tempStatus.text = "Invalid Port!"
            return
        }

        val ipAdvanced = findViewById<EditText>(R.id.ipEditAdvanced).text.toString()
        val portAdvanced = Integer.valueOf(findViewById<EditText>(R.id.portEdit).text.toString())
        val messageAdvanced = findViewById<EditText>(R.id.messageEdit).text.toString()
        var senderAdvanced : OSCAdvanced? = null
        val tempTag = view.tag.toString()
        if(tempTag == "String") {
            senderAdvanced = OSCAdvanced(ipAdvanced, portAdvanced, messageAdvanced, findViewById<EditText>(R.id.parameterEdit).text.toString())
        } else if(tempTag == "Float"){
            try{
                senderAdvanced = OSCAdvanced(ipAdvanced, portAdvanced, messageAdvanced, findViewById<EditText>(R.id.parameterEdit).text.toString().toFloat())
            } catch(e : NumberFormatException){
                tempStatus.text = "Parameter not a Float!"
            }
        } else if(tempTag == "Integer"){
            try{
                senderAdvanced = OSCAdvanced(ipAdvanced, portAdvanced, messageAdvanced, findViewById<EditText>(R.id.parameterEdit).text.toString().toInt())
            } catch(e : NumberFormatException){
                tempStatus.text = "Parameter not an Integer!"
            }
        } else if(tempTag == "Boolean"){
            try{
                senderAdvanced = OSCAdvanced(ipAdvanced, portAdvanced, messageAdvanced, findViewById<EditText>(R.id.parameterEdit).text.toString().toBooleanStrict())
            } catch(e : IllegalArgumentException){
                tempStatus.text = "Parameter not \"true\" or \"false\"!"
            }
        }

        senderAdvanced?.start()
    }

    //TROY: Function that ends current activity to return to MainActivity. Also puts values in shared preferences.
    fun returnAdvanced(view: View){
        val returnIntent = Intent()

        val persistent = applicationContext.getSharedPreferences("IP Storage", MODE_PRIVATE)
        val editor = persistent.edit()
        editor.putString("ipAdvanced", findViewById<EditText>(R.id.ipEditAdvanced).text.toString())
        editor.putString("portAdvanced", findViewById<EditText>(R.id.portEdit).text.toString())
        editor.putString("messageAdvanced", findViewById<EditText>(R.id.messageEdit).text.toString())
        editor.putString("parameterAdvanced", findViewById<EditText>(R.id.parameterEdit).text.toString())
        editor.apply()

        setResult(RESULT_OK, returnIntent)
        finish()
    }
}