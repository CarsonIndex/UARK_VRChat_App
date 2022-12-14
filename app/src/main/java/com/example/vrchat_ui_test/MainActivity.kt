package com.example.vrchat_ui_test

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService


class MainActivity : AppCompatActivity() {
    private var ip:String = "127.0.0.1"
    private var checkPersist = 0
    private lateinit var muteToggle: ToggleButton
    private lateinit var mute: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        muteToggle = findViewById(R.id.MuteToggle)
        mute = findViewById(R.id.Mute)
        appContext = applicationContext

        val persistent = applicationContext.getSharedPreferences("IP Storage", MODE_PRIVATE)
        val persistent_box = applicationContext.getSharedPreferences("Checkbox Storage", MODE_PRIVATE)
        if(persistent.getString("ipAddress", "") != null) ip = persistent.getString("ipAddress", "").toString()
        if(persistent_box.getString("pushToTalk", "") == "true")
            changeTalkButton("true")

        val buttonList = arrayOf(R.id.LeftSpin, R.id.UpArrow, R.id.RightSpin, R.id.LeftArrow, R.id.RightArrow, R.id.DownArrow, R.id.Sprint, R.id.MuteToggle)

        //for(buttonID in buttonList){
        //    addTouch(findViewById<Button>(buttonID))
        //}

        for(buttonID in buttonList){
            addCheckChange(findViewById(buttonID))
        }

    }

    fun launchSetting(view: View){
        val settingIntent = Intent(this, SettingActivity::class.java)

        settingIntent.putExtra("ipAddress", ip)
        settingIntent.putExtra("checkBool", checkPersist.toString())

        startActivityForResult(settingIntent, 1)
    }

    fun launchAdvanced(view: View){
        val advancedIntent = Intent(this, AdvancedActivity::class.java)

        startActivity(advancedIntent)
    }

    fun launchChat(view: View){
        val chatIntent = Intent(this, ChatActivity::class.java)

        chatIntent.putExtra("ipAddress", ip)

        startActivity(chatIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        ip = data!!.getStringExtra("ipAddress")!!
        val pushToTalk = data!!.getStringExtra("pushToTalk")
        if (pushToTalk != null) {
            changeTalkButton(pushToTalk)
        }
    }

    fun changeTalkButton(pushToTalk: String) {
        if (pushToTalk == "true") {
            mute.visibility = View.GONE
            muteToggle.visibility = View.VISIBLE
            checkPersist = 1
        }
        else {
            mute.visibility = View.VISIBLE
            muteToggle.visibility = View.GONE
            checkPersist = 0
        }
    }

    fun buttonOnPress(view: View){
        val tag:String = view.tag as String
        val model = OSCModel(ip, tag, true)

        model.start()

        when(tag) {
            "voiceOn" -> {
                view.tag = "voiceOff"
                Log.d("voice","sendVoice(false)")
            }
            "voiceOff" -> {
                view.tag = "voiceOn"
                Log.d("voice","sendVoice(true)")
            }
            "safeOn" -> {
                view.tag = "safeOff"
                Log.d("safe","sendSafe(false)")
            }
            "safeOff" -> {
                view.tag = "safeOn"
                Log.d("safe","sendSafe(true)")
            }
        }
    }

    fun addTouch(element: Button){  //TROY: This implementation of detecting when a button is released seems to conflict with the touch detection for the UI.
        element.setOnTouchListener(object:View.OnTouchListener{ //TROY: When this is used with a button, the ripple effect that normally shows up when it's pressed is disabled.
            override fun onTouch(v:View, event:MotionEvent):Boolean{
                val tag:String = v.tag as String
                val pressed:Boolean = (event.getAction() != MotionEvent.ACTION_UP)
                val model = OSCModel(ip, tag, pressed)

                model.start()

                return true
            }
        })
    }

     fun onRelease(v:View, event:MotionEvent):Boolean{  //TROY: If you ever figure out how to have a function be run on button release, use this.
        val tag:String = v.tag as String
        val pressed:Boolean = (event.getAction() != MotionEvent.ACTION_UP)
        val model = OSCModel(ip, tag, pressed)

        model.start()

        return true
    }

    private fun addCheckChange(element: ToggleButton){  //TROY: If you ever figure out how to have a function be run on button release, use this.
        element.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked ->
            val tag:String = buttonView.tag as String
            val model = OSCModel(ip, tag, isChecked)

            if (isChecked) {
                buttonView.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.teal_700)))
            } else {
                buttonView.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.square)))
            }

            model.start()
        }
    }

    companion object{
        lateinit var appContext: Context

        fun isNetworkAvailable(): Boolean {
            val connector = getSystemService(appContext, ConnectivityManager::class.java)
            val netInfo = connector?.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }
    }
}