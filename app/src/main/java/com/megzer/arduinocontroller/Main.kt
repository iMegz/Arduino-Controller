package com.megzer.arduinocontroller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Controls.hideSystemBars(window)
        setContentView(R.layout.main)

        //--------------------------------- Controller button ---------------------------------//
        findViewById<Button>(R.id.btnController).setOnClickListener {
            val intent = Intent(this, Controller::class.java)
            startActivity(intent)
            finish()
        }
        //-------------------------------------------------------------------------------------//


        //-------------------------------- Configuration button -------------------------------//
        findViewById<Button>(R.id.btnConfig).setOnClickListener {
            val intent = Intent(this, Config::class.java)
            startActivity(intent)
            finish()
        }
        //-------------------------------------------------------------------------------------//
    }
}

