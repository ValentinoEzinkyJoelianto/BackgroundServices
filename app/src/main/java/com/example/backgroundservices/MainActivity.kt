package com.example.backgroundservices

import android.Manifest
import android.content.Intent
import android.hardware.Sensor
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var btnStart: Button

    companion object{
        const val ACTION_SERVICE_STOPPED_JUMP =  "nit2x.paba.backgroundservice.ACTION_SERVICE_STOPPED_JUMP"
        const val REQUEST_CODE_PERMISSIONS = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.statusTextView)
        btnStart = findViewById(R.id.btnStart)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnStart.setOnClickListener{
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest,
                REQUEST_CODE_PERMISSIONS)
        }
        handleIntent(intent)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            var allPermissionsGranted = true
            for (result in grantResults) {
                if (result != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }

            if (allPermissionsGranted) {
                val serviceIntent = Intent(this, TrackerService::class.java)
                startService(serviceIntent)
                finish()
            } else {
                statusTextView.text = "Izin diperlukan untuk menjalankan layanan."
            }
        }
    }
    private fun handleIntent(intent: Intent){
        if(intent.action == ACTION_SERVICE_STOPPED_JUMP){
            statusTextView.text =  "Background services sudah berhenti karena Anda melompat"
        } else {
        }
    }
    override fun onNewIntent (intent: Intent){
        super.onNewIntent(intent)
        intent.let{
            handleIntent(it)
        }
    }
    private val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}