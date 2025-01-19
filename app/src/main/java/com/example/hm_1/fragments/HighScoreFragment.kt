package com.example.hm_1.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hm_1.Interface.HighScoreCallBack
import com.example.hm_1.logic.Player
import com.example.hm_1.R
import com.google.android.material.button.MaterialButton

class HighScoreFragment : Fragment() {

    private lateinit var highScoreTable: TableLayout
    var onLocationClicked: HighScoreCallBack? = null
    var restartGame : HighScoreCallBack? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_score, container, false)
        findViews(v)

        val startOverBtn = v.findViewById<MaterialButton>(R.id.Start_Over_BTN)
        startOverBtn.setOnClickListener {
            restartGame?.restartGame()
        }

        val players = arguments?.getParcelableArrayList<Player>("players") ?: arrayListOf()
        displayScores(players)

        return v
    }

    private fun findViews(v: View) {
        highScoreTable = v.findViewById(R.id.score_table)
    }

    private fun displayScores(players: List<Player>) {
        highScoreTable.removeViews(1, highScoreTable.childCount - 1)

        for ((index, player) in players.withIndex()) {
            val row = TableRow(requireContext())

            val rankView = TextView(requireContext()).apply {
                text = (index + 1).toString()
                gravity = Gravity.CENTER
                setPadding(8, 8, 8, 8)
                setTextColor(resources.getColor(R.color.neon_light_blue, null))
                textSize = 16f
            }

            val nameView = TextView(requireContext()).apply {
                text = player.name
                gravity = Gravity.CENTER
                setPadding(8, 8, 8, 8)
                setTextColor(resources.getColor(R.color.neon_light_blue, null))
                textSize = 16f
            }

            val scoreView = TextView(requireContext()).apply {
                text = player.score.toString()
                gravity = Gravity.CENTER
                setPadding(8, 8, 8, 8)
                setTextColor(resources.getColor(R.color.neon_light_blue, null))
                textSize = 16f
            }

            val locationButton = ImageButton(requireContext()).apply {
                setImageResource(R.drawable.ic_map_pin)
                setBackgroundColor(Color.TRANSPARENT)
                setOnClickListener {
                    onLocationClicked?.onLocationClicked(player.name)
                }
            }

            row.addView(rankView)
            row.addView(nameView)
            row.addView(scoreView)
            row.addView(locationButton)

            highScoreTable.addView(row)
        }
    }
}
