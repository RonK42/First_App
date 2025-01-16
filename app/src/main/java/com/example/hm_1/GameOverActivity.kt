package com.example.hm_1

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hm_1.fragments.HighScoreFragment
import com.example.hm_1.Interface.HighScoreCallBack

class GameOverActivity : AppCompatActivity() {

    private val scores = mutableListOf<Int>()
    private lateinit var main_FRAME_list: FrameLayout


    private lateinit var highScoreFragment: HighScoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        val score = intent?.getIntExtra("score", -1) ?: -1
        if (score == -1) {
            Log.e("GameOverActivity", "No score passed in Intent")
            finish()
            return
        }


        val scores = loadScores()
        scores.add(score)
        scores.sortDescending()
        saveScores(scores)

        Log.d("GameOverActivity", "Scores after update: $scores")


        val fragment = HighScoreFragment()
        val bundle = Bundle()
        bundle.putIntegerArrayList("scores", ArrayList(scores))
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_FRAME_list, fragment)
            .commit()
    }

    private fun saveScores(scores: MutableList<Int>) {
        val sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val scoresString = scores.joinToString(",")
        editor.putString("scores", scoresString)
        editor.apply()
    }

    private fun loadScores(): MutableList<Int> {
        val sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE)
        val scoresString = sharedPreferences.getString("scores", "") ?: ""
        return if (scoresString.isNotEmpty()) {
            scoresString.split(",").map { it.toInt() }.toMutableList()
        } else {
            mutableListOf()
        }
    }


    private fun initViews() {
        highScoreFragment = HighScoreFragment()
        highScoreFragment.onLocationClicked = object : HighScoreCallBack {
            override fun onLocationClicked(playerName: String) {
            }

        }
    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)

    }
}