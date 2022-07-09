package com.megzer.arduinocontroller

import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class Controls {
    companion object{
        var front = 'w'
        var back = 's'
        var left = 'a'
        var right = 'd'
        var front_right = 'e'
        var front_left = 'q'
        var back_right = 'x'
        var back_left = 'z'
        var rotate_clockwise = 'r'
        var rotate_anticlockwise = 't'
        var fire = 'f'
        var open = 'o'
        var close = 'c'
        var speed = 'm'
        var arm_up = 'u'
        var arm_down = 'j'
        var arm_right = 'k'
        var arm_left = 'h'

        fun hideSystemBars(window: Window) {
            val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}