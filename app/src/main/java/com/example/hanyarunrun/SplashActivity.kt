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
        setContentView(R.layout.activity_splash)


        lifecycleScope.launch {
            delay(2000L)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}