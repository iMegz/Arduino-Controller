package com.megzer.arduinocontroller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.*
import androidx.core.text.HtmlCompat
import io.github.controlwear.virtual.joystick.android.JoystickView
import com.megzer.arduinocontroller.Controls.Companion as c
import com.megzer.arduinocontroller.Bluetooth.Companion as b

class Controller : AppCompatActivity() {
    var readingThread: Thread? = null
    var read = false
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.controller)
        c.hideSystemBars(window)
        if(b.connectedDevice != null) findViewById<TextView>(R.id.deviceName).text = b.connectedDevice!!.name
        else findViewById<TextView>(R.id.deviceName).text = "No connection"


        //------------------------------------ Back button ------------------------------------//
        findViewById<ImageButton>(R.id.backToMainMenu).setOnClickListener {
            val intent = Intent(this, Main::class.java)
            startActivity(intent)
            finish()
        }
        //-------------------------------------------------------------------------------------//


        //----------------------------------- Sensors names ----------------------------------//
        findViewById<TextView>(R.id.s1).text = resources.getString(R.string.sensor_name, c.s1)
        findViewById<TextView>(R.id.s2).text = resources.getString(R.string.sensor_name, c.s2)
        findViewById<TextView>(R.id.s3).text = resources.getString(R.string.sensor_name, c.s3)
        findViewById<TextView>(R.id.s4).text = resources.getString(R.string.sensor_name, c.s4)
        //-------------------------------------------------------------------------------------//


        //------------------------------- Direction mode toggle -------------------------------//
        val directionsToggleButton = findViewById<ToggleButton>(R.id.directionsToggle)
        //-------------------------------------------------------------------------------------//


        //--------------------------------- Bluetooth button ----------------------------------//
        val bButton = findViewById<ImageButton>(R.id.bluetooth)
        bButton.setOnClickListener{
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
            }else{
                sendCmd(c.stop)
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
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seek: SeekBar?) {}
            override fun onStopTrackingTouch(seek: SeekBar?) {
                val progress = seek?.progress
                val cmd = "${c.speed}:$progress;"
                sendCmd(cmd)
            }
        })
        //-------------------------------------------------------------------------------------//


        //------------------------- Arm vertical movement controller --------------------------//
        val armVerticalMovementController = findViewById<SeekBar>(R.id.armVerticalMovementController)
        armVerticalMovementController.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seek: SeekBar?) {}
            override fun onStopTrackingTouch(seek: SeekBar?) {
                val progress = seek?.progress
                val cmd = "${c.arm_vertical_movement}:$progress;"
                sendCmd(cmd)
            }
        })
        //-------------------------------------------------------------------------------------//


        //--------------------------- Enable reading from sensors -----------------------------//
        val enableSensorsToggle = findViewById<ToggleButton>(R.id.enableSensorsToggle)
        enableSensorsToggle.setOnCheckedChangeListener{ _, checked ->
            if(checked) {
                sendCmd(c.scan_on)
                read = true
                readingThread = Thread{
                    while (b.isInit() && b.bAdapter.isEnabled && b.connectedDevice != null && read){
                        try {
                            var sensor = 0
                            var data = ""

                            //Get sensor number
                            var c = b.bInputStream.read()
                            while (c.toChar() != ':'){
                                sensor = c - 48
                                c = b.bInputStream.read()
                            }

                            //Get sensor value
                            c = b.bInputStream.read()
                            while (c.toChar() != ';'){
                                data += c.toChar()
                                c = b.bInputStream.read()
                            }

                            updateSensorReading(sensor, data.toInt())
                            Thread.sleep(60)
                        } catch (e:InterruptedException){
                            Toast.makeText(this, "Reading sensors disabled", Toast.LENGTH_SHORT).show()
                        }catch (e:Exception){
                            b.reconnect(b.connectedDevice!!, 1)
                            Log.v("Error", e.printStackTrace().toString())
                            readingThread = null
                        }
                    }
                }
                readingThread?.start()
            }
            else {
                read = false
                sendCmd(c.scan_off)
                readingThread = null
            }
        }
        //-------------------------------------------------------------------------------------//



    }

    private fun sendCmd(cmd:String){
        if(b.isInit() && b.bAdapter.isEnabled && b.connectedDevice != null && !b.reconnecting){
            try {
                b.bOutputStream.write(cmd.toByteArray())
            }catch (e:Exception){
                b.reconnect(b.connectedDevice!!)
                //b.connectedDevice = null
                //findViewById<TextView>(R.id.deviceName).text = "No connection"
            }
        }
    }
    private fun sendCmd(cmd:Char) = sendCmd(cmd.toString())

    private fun updateSensorReading(sensor:Int, data:Int){
        when(sensor){
            1-> findViewById<TextView>(R.id.s1value).text = data.toString()
            2-> findViewById<TextView>(R.id.s2value).text = data.toString()
            3-> findViewById<TextView>(R.id.s3value).text = data.toString()
            4-> findViewById<TextView>(R.id.s4value).text = data.toString()
        }
    }


}