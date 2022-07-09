package com.megzer.arduinocontroller

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Animatable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Controls.hideSystemBars(window)

        val splashScreen: ImageView = findViewById(R.id.megzer)
        (splashScreen.drawable as? Animatable)?.start()

        splashScreen.setOnLongClickListener{
            handler.removeCallbacksAndMessages(null)
            startMain()
            true
        }

        handler.postDelayed({
            startMain()
        },6500)
    }

    private fun startMain(){
        val intent = Intent(this, Main::class.java)
        startActivity(intent)
        finish()
    }
}