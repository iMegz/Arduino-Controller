package com.megzer.arduinocontroller

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spanned
import android.view.MotionEvent
import android.view.Window
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.lang.IllegalArgumentException

class Controls {
    companion object{
        var front = 'w'
        var back = 's'
        var left = 'a'
        var right = 'd'
        var stop = 'b'
        var front_right = 'e'
        var front_left = 'q'
        var back_right = 'x'
        var back_left = 'z'
        var rotate_clockwise = 'r'
        var rotate_anticlockwise = 't'
        var fire = 'f'
        var open_arm = 'o'
        var close_arm = 'c'
        var speed = 'm'
        var arm_vertical_movement = 'v'
        var scan_on = 'u'
        var scan_off = 'p'
        var s1 = "Front"
        var s2 = "Right"
        var s3 = "Left"
        var s4 = "Back"
        var s1value = 0
        var s2value = 0
        var s3value = 0
        var s4value = 0

        fun hideSystemBars(window: Window) {
            val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }

        fun dialog(activity: AppCompatActivity, msg:String){
            val builder = activity.let { AlertDialog.Builder(it) }
            builder.apply {
                setTitle("Info")
                setMessage(msg)
                setCancelable(true)
            }
            builder.create().show()
        }

        fun dialog(activity: AppCompatActivity, msg:Spanned){
            val builder = activity.let { AlertDialog.Builder(it) }
            builder.apply {
                setTitle("Info")
                setMessage(msg)
                setCancelable(true)
            }
            builder.create().show()
        }

        @SuppressLint("ClickableViewAccessibility")
        fun setClickEffect(b: ImageButton, color:String = "#004080" ){
            val parsedColor =
                try { Color.parseColor(color) }
                catch (e: IllegalArgumentException){ Color.parseColor("#004080") }
            val filter = b.colorFilter
            b.setOnTouchListener { _, motionEvent ->
                when(motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        b.setColorFilter(parsedColor)
                    }
                    MotionEvent.ACTION_UP ->{
                        b.colorFilter = filter
                    }
                }
                false
            }
        }
    }
}