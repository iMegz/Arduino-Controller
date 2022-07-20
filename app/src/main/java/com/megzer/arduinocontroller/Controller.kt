package com.megzer.arduinocontroller

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.text.HtmlCompat
import com.google.android.material.slider.Slider
import io.github.controlwear.virtual.joystick.android.JoystickView
import com.megzer.arduinocontroller.Controls.Companion as c

class Controller : AppCompatActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.controller)
        c.hideSystemBars(window)
        if(Bluetooth.connectedDevice != null) findViewById<TextView>(R.id.deviceName).text = Bluetooth.connectedDevice!!.name
        else findViewById<TextView>(R.id.deviceName).text = "No connection"


        //------------------------------------ Back button ------------------------------------//
        findViewById<ImageButton>(R.id.backToMainMenu).setOnClickListener {
            val intent = Intent(this, Main::class.java)
            startActivity(intent)
            finish()
        }
        //-------------------------------------------------------------------------------------//


        //------------------------------- Direction mode toggle -------------------------------//
        val directionsToggleButton = findViewById<ToggleButton>(R.id.directionsToggle)
        //-------------------------------------------------------------------------------------//


        //--------------------------------- Bluetooth button ----------------------------------//
        val bluetoothButton = findViewById<ImageButton>(R.id.bluetooth)
        bluetoothButton.setOnClickListener{
            val intent = Intent(this, Bluetooth::class.java)
            startActivity(intent)
            finish()
        }
        //-------------------------------------------------------------------------------------//


        //-------------------------------- Movement info button -------------------------------//
        val movementInfoButton= findViewById<ImageButton>(R.id.movementInfo)
        movementInfoButton.setOnClickListener {
            val msgHtml = "Moving North East will send :<br>" +
                    "<font color = 'blue'>4 Directions : </font>2 commands : '${c.front}' -> '${c.right}'<br>" +
                    "<font color = 'blue'>8 Directions : </font>1 command  : '${c.front_right}'<br>" +
                    "<font color = 'red'>Note : </font> 8 Directions is recommended when using Omni wheels"
            val msg = HtmlCompat.fromHtml(msgHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
            Controls.dialog(this, msg)
        }
        c.setClickEffect(movementInfoButton, "#aaaaaa")
        //-------------------------------------------------------------------------------------//


        //-------------------------------- Movement controller --------------------------------//
        val movementController = findViewById<JoystickView>(R.id.movementController)
        movementController.setOnMoveListener{ angle, strength ->
            val dir8 = directionsToggleButton.isChecked
            if(strength > 50){

                if(angle < 22.5 || angle >= 337.5) { // East

                    sendCmd(c.right)

                }else if(angle < 67.5){ // North east

                    if(dir8) sendCmd(c.front_right)
                    else {
                        sendCmd(c.front)
                        sendCmd(c.right)
                    }

                }else if(angle < 112.5) { // North

                    sendCmd(c.front)

                }else if(angle < 157.5){ // North west

                    if(dir8) sendCmd(c.front_left)
                    else {
                        sendCmd(c.front)
                        sendCmd(c.left)
                    }

                }else if(angle < 202.5) { //West

                    sendCmd(c.left)

                }else if(angle < 247.5){ //South west

                    if(dir8) sendCmd(c.back_left)
                    else {
                        sendCmd(c.back)
                        sendCmd(c.left)
                    }

                }else if(angle < 292.5) { //South

                    sendCmd(c.back)

                }else if(angle < 337.5){ //South east

                    if(dir8) sendCmd(c.back_right)
                    else {
                        sendCmd(c.back)
                        sendCmd(c.right)
                    }

                }
            }
        }
        //-------------------------------------------------------------------------------------//


        //--------------------------------------- Fire ----------------------------------------//
        val fireButton = findViewById<ImageButton>(R.id.fireBtn)
        c.setClickEffect(fireButton, "#ff0000")
        fireButton.setOnClickListener { sendCmd(c.fire) }
        //-------------------------------------------------------------------------------------//


        //--------------------------------- Arm base rotation ---------------------------------//
        val rotateAntiClockwiseButton = findViewById<ImageButton>(R.id.rotateAntiClockwiseBtn)
        val rotateClockwiseButton = findViewById<ImageButton>(R.id.rotateClockwiseBtn)

        c.setClickEffect(rotateAntiClockwiseButton, "#aaaaaa")
        c.setClickEffect(rotateClockwiseButton, "#aaaaaa")

        rotateAntiClockwiseButton.setOnClickListener { sendCmd(c.rotate_anticlockwise) }
        rotateClockwiseButton.setOnClickListener { sendCmd(c.rotate_clockwise) }
        //-------------------------------------------------------------------------------------//


        //--------------------------------- Open / Close arm ----------------------------------//
        val openCloseArmToggleButton = findViewById<ToggleButton>(R.id.openCLoseArmToggle)
        openCloseArmToggleButton.setOnCheckedChangeListener{ _, checked ->
            if(checked) sendCmd(c.open_arm)
            else sendCmd(c.close_arm)
        }
        //-------------------------------------------------------------------------------------//


        //--------------------------------- Speed controller ----------------------------------//
        val speedController = findViewById<SeekBar>(R.id.speedController)
        speedController.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {
                val cmd = "${c.speed}:$progress;"
                sendCmd(cmd)
            }
            override fun onStartTrackingTouch(seek: SeekBar?) {}
            override fun onStopTrackingTouch(seek: SeekBar?) {}
        })
        //-------------------------------------------------------------------------------------//


        //------------------------- Arm vertical movement controller --------------------------//
        val armVerticalMovementController = findViewById<SeekBar>(R.id.armVerticalMovementController)
        armVerticalMovementController.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {
                val cmd = "${c.arm_vertical_movement}:$progress;"
                sendCmd(cmd)
            }
            override fun onStartTrackingTouch(seek: SeekBar?) {}
            override fun onStopTrackingTouch(seek: SeekBar?) {}
        })
        //-------------------------------------------------------------------------------------//



    }

    private fun sendCmd(cmd:String){
        if(Bluetooth.bAdapter.isEnabled && Bluetooth.connectedDevice != null){
            try {
                
            }catch (e:Exception){

            }
        }
    }
    private fun sendCmd(cmd:Char) = sendCmd(cmd.toString())

}