package com.megzer.arduinocontroller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat

class Controller : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.controller)
        Controls.hideSystemBars(window)

        //Back button
        findViewById<ImageButton>(R.id.backToMainMenu).setOnClickListener {
            val intent = Intent(this, Main::class.java)
            startActivity(intent)
            finish()
        }

        //Movement info button
        val movementInfoBtn = findViewById<ImageButton>(R.id.movementInfo)
        movementInfoBtn.setOnClickListener {
            val msgHtml = "Moving North East will send :<br>" +
                    "<font color = 'blue'>4 Directions : </font>2 commands : '${Controls.front}' -> '${Controls.right}'<br>" +
                    "<font color = 'blue'>8 Directions : </font>1 command  : '${Controls.front_right}'<br>" +
                    "<font color = 'red'>Note : </font> 8 Directions is recommended when using Omni wheels"
            val msg = HtmlCompat.fromHtml(msgHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
            Controls.dialog(this, msg)
        }
        Controls.setClickEffect(movementInfoBtn, "#fff")


    }


}