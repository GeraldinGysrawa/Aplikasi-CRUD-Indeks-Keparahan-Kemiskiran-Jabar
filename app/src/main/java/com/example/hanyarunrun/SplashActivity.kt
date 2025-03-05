package com.example.hanyarunrun

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Pastikan ada XML untuk tampilan Splash

        // Delay splash screen selama 2 detik
        lifecycleScope.launch {
            delay(2000L)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // Tutup SplashActivity agar tidak bisa dikembalikan dengan back button
        }
    }
}