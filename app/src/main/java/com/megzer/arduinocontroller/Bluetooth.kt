package com.megzer.arduinocontroller

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.w3c.dom.Text
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class Bluetooth : AppCompatActivity() {
    companion object{
        lateinit var bAdapter: BluetoothAdapter
        lateinit var bSocket: BluetoothSocket
        var connectedDevice: BluetoothDevice? = null
    }
    private val devicesList:ArrayList<BluetoothDevice> = ArrayList()
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    lateinit var devicesListView:ListView
    lateinit var arrayAdapter: ArrayAdapter<String>
    private val handler: Handler = Handler(Looper.getMainLooper())

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetooth)
        Controls.hideSystemBars(window)


        //------------------------------------ Back button ------------------------------------//
        findViewById<ImageButton>(R.id.backToMainMenu).setOnClickListener {
            val intent = Intent(this, Controller::class.java)
            startActivity(intent)
            finish()
        }
        //-------------------------------------------------------------------------------------//


        //----------------------------- Devices list and connection ---------------------------//
        devicesListView = findViewById(R.id.devicesList)
        devicesListView.setOnItemClickListener{ _, child, position, _ ->
            val device = devicesList[position]
            val deviceInView = child as TextView
            var connecting = true

            val toast = Toast.makeText(this, "Device is not available", Toast.LENGTH_SHORT)

            val animationThread = Thread{
                val name = device.name
                while (connecting){
                    for(i in 1 .. 3){
                        try {
                            deviceInView.text = "$name (Connecting${".".repeat(i)})"
                            Thread.sleep(250)
                        }catch (e:InterruptedException){
                            break
                        }
                    }
                }
            }

            val connectionThread = Thread {
                try {
                    bAdapter.cancelDiscovery()
                    bSocket = device.createRfcommSocketToServiceRecord(uuid)
                    bSocket.connect()
                    connectedDevice = device
                    connecting = false
                    animationThread.interrupt()
                    deviceInView.text = "${device.name} (Connected)"
                } catch (e: Exception) {
                    toast.show()
                    connecting = false
                    animationThread.interrupt()
                    deviceInView.text = "${device.name} (Failed to connect)"
                    Thread.sleep(1000)
                    deviceInView.text = device.name
                }
            }
            connectionThread.start()
            animationThread.start()

        }
        //-------------------------------------------------------------------------------------//


        //------------------------------ Bluetooth initialization -----------------------------//
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        bAdapter = bluetoothManager.adapter
        if(bAdapter.isEnabled){
            devicesList.clear()
            for(device in bAdapter.bondedDevices){
                devicesList.add(device)
            }
            updateDevicesList()
        }
        //-------------------------------------------------------------------------------------//


        //------------------------------- Enable bluetooth button -----------------------------//
        val enableBluetoothButton = findViewById<Button>(R.id.enableBtn)
        enableBluetoothButton.setOnClickListener {
            if(!bAdapter.isEnabled){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requestMultiplePermissions.launch(arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ))
                }
                else{
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    requestBluetoothPermission.launch(enableBtIntent)
                }
            }else Toast.makeText(this,"Bluetooth is enabled", Toast.LENGTH_SHORT).show()
        }
        //-------------------------------------------------------------------------------------//


        //----------------------------- Get paired devices ----------------------------//
        val getPairedDevicesButton = findViewById<Button>(R.id.getPairedDevices)
        getPairedDevicesButton.setOnClickListener {
            if(bAdapter.isEnabled){
                devicesList.clear()
                for(device in bAdapter.bondedDevices){
                    devicesList.add(device)
                }
                updateDevicesList()
            }
        }
        //-------------------------------------------------------------------------------------//


        //--------------------------------- Pair to new device --------------------------------//
        val pairToNewDeviceButton = findViewById<Button>(R.id.pairToNewDevice)
        pairToNewDeviceButton.setOnClickListener {
            val intentOpenBluetoothSettings = Intent()
            intentOpenBluetoothSettings.action = Settings.ACTION_BLUETOOTH_SETTINGS
            startActivity(intentOpenBluetoothSettings)
        }
        //-------------------------------------------------------------------------------------//

    }

    private var requestBluetoothPermission = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}

    @SuppressLint("MissingPermission")
    private fun updateDevicesList(){
        val empty = findViewById<TextView>(R.id.empty)
        if(devicesList.isEmpty())  {
            empty.visibility = View.VISIBLE
            arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, arrayListOf())
            devicesListView.adapter = arrayAdapter
            arrayAdapter.notifyDataSetChanged()
        }
        else{
            empty.visibility = View.INVISIBLE
            val stringListOfDevices: List<String> = devicesList.map { it.name }
            arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, stringListOfDevices)
            devicesListView.adapter = arrayAdapter
            arrayAdapter.notifyDataSetChanged()
        }
    }
}