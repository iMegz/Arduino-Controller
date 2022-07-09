package com.megzer.arduinocontroller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Controller : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.controller)
        Controls.hideSystemBars(window)
        findViewById<ImageButton>(R.id.backToMainMenu).setOnClickListener {
            val intent = Intent(this, Main::class.java)
            startActivity(intent)
            finish()
        }
    }
}