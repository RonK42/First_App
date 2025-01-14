package com.example.hm_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainMenuActivity : AppCompatActivity() {

    private lateinit var chipGroupModes: ChipGroup
    private lateinit var chipGroupDifficulty: ChipGroup
    private lateinit var btnStartGame: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acticity_menu)
        findViews()
        initViews()
    }

    private fun findViews() {
        chipGroupModes = findViewById(R.id.chipGroupModes)
        chipGroupDifficulty = findViewById(R.id.chipGroupDifficulty)
        btnStartGame = findViewById(R.id.btnStartGame)
    }

    private fun initViews() {
        btnStartGame.setOnClickListener {
            val selectedModeId = chipGroupModes.checkedChipId
            val selectedDifficultyId = chipGroupDifficulty.checkedChipId

            val mode = when (selectedModeId) {
                R.id.chipTilt -> "TILT_MODE"
                R.id.chipArrows -> "BUTTONS_MODE"
                else -> null
            }

            val difficulty = when (selectedDifficultyId) {
                R.id.chipEasy -> "EASY"
                R.id.chipHard -> "HARD"
                else -> null
            }

            if (mode != null && difficulty != null) {
                moveToGameActivity(mode, difficulty)
            } else {
                // Handle missing selections (e.g., show a toast)
            }
        }
    }

    private fun moveToGameActivity(mode: String, difficulty: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("MODE", mode)
        intent.putExtra("DIFFICULTY", difficulty)
        startActivity(intent)
        finish()
    }
}