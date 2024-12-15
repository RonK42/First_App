package com.example.hm_1

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView

lateinit var game_over : ImageView

class gameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)
        findViews()
        initViews()
    }

    private fun initViews() {
        game_over.visibility = View.VISIBLE
    }

    private fun findViews() {
        game_over = findViewById(R.id.game_over)
    }
}
