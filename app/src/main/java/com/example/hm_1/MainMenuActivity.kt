package com.example.hm_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainMenuActivity : AppCompatActivity() {
    private lateinit var Buttons_Button: ExtendedFloatingActionButton
    private lateinit var Tilt_Button: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acticity_menu)
        findViews()
        initViews()
    }

    private fun findViews() {
        Buttons_Button = findViewById(R.id.Buttons_Button)
        Tilt_Button = findViewById(R.id.Tilt_Button)
    }

    private fun initViews() {
        Buttons_Button.setOnClickListener { moveToGameActivity("BUTTONS_MODE") }
        Tilt_Button.setOnClickListener { moveToGameActivity("TILT_MODE") }
    }

    private fun moveToGameActivity(mode: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("MODE", mode)
        startActivity(intent)
        finish()
    }
}
