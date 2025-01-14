package com.example.hm_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MainMenuActivity : AppCompatActivity() {
    private lateinit var chipGroupModes: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acticity_menu)
        findViews()
        initViews()
    }

    private fun findViews() {
        chipGroupModes = findViewById(R.id.chipGroupModes)
    }

    private fun initViews() {
        chipGroupModes.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipTilt -> moveToGameActivity("TILT_MODE")
                R.id.chipArrows -> moveToGameActivity("BUTTONS_MODE")
            }
        }
    }

    private fun moveToGameActivity(mode: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("MODE", mode)
        startActivity(intent)
        finish()
    }
}
