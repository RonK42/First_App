package com.example.hm_1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class LoadingScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screen)

        // Delay for a shorter time (e.g., 1 second)
        Handler().postDelayed({
            // Start MainActivity
            val intent = Intent(this@LoadingScreen, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the LoadingScreen activity
        }, 1000) // 1000ms = 1 second
    }
}
