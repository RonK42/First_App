package com.example.hm_1.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import com.example.hm_1.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.example.hm_1.Interface.HighScoreCallBack

class HighScoreFragment : Fragment() {
    private lateinit var playerNameOne: TextView
    private lateinit var playerNameTwo: TextView
    private lateinit var playerNameThree: TextView
    private lateinit var playerNameFour: TextView
    private lateinit var playerNameFive: TextView
    private lateinit var playerNameSix: TextView
    private lateinit var playerNameSeven: TextView
    private lateinit var playerNameEight: TextView
    private lateinit var playerNameNine: TextView
    private lateinit var playerNameTen: TextView
    private lateinit var playerScoreOne: TextView
    private lateinit var playerScoreTwo: TextView
    private lateinit var playerScoreThree: TextView
    private lateinit var playerScoreFour: TextView
    private lateinit var playerScoreFive: TextView
    private lateinit var playerScoreSix: TextView
    private lateinit var playerScoreSeven: TextView
    private lateinit var playerScoreEight: TextView
    private lateinit var playerScoreNine: TextView
    private lateinit var playerScoreTen: TextView
    private lateinit var loc_BTN_one: ImageButton
    private lateinit var loc_BTN_two: ImageButton
    private lateinit var loc_BTN_three: ImageButton
    private lateinit var loc_BTN_four: ImageButton
    private lateinit var loc_BTN_five: ImageButton
    private lateinit var loc_BTN_six: ImageButton
    private lateinit var loc_BTN_seven: ImageButton
    private lateinit var loc_BTN_eight: ImageButton
    private lateinit var loc_BTN_nine: ImageButton
    private lateinit var loc_BTN_ten: ImageButton
    private val scores = mutableListOf<Int>()
    private lateinit var highScoreTable: TableLayout
    var onLocationClicked: HighScoreCallBack? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_score, container, false)
        findViews(v)


        val scoresFromBundle = arguments?.getIntegerArrayList("scores") ?: arrayListOf()
        scores.clear()
        scores.addAll(scoresFromBundle)

        displayScores()
        return v
    }


    private fun displayScores() {

        while (highScoreTable.childCount - 1 < scores.size) {

            val templateRow = highScoreTable.getChildAt(1) as TableRow
            val newRow = TableRow(requireContext())


            for (i in 0 until templateRow.childCount) {
                val view = templateRow.getChildAt(i)
                val newView = when (view) {
                    is TextView -> TextView(requireContext()).apply {
                        layoutParams = view.layoutParams
                        textSize = view.textSize / resources.displayMetrics.scaledDensity
                        setTextColor(view.currentTextColor)
                        gravity = view.gravity
                    }

                    is ImageButton -> ImageButton(requireContext()).apply {
                        layoutParams = view.layoutParams
                        setImageDrawable(view.drawable)
                        setBackgroundColor(Color.TRANSPARENT)
                    }

                    else -> null
                }
                newView?.let { newRow.addView(it) }
            }

            highScoreTable.addView(newRow)
        }


        for (index in scores.indices) {
            val row = highScoreTable.getChildAt(index + 1) as TableRow
            val rankView = row.getChildAt(0) as TextView
            val nameView = row.getChildAt(1) as TextView
            val scoreView = row.getChildAt(2) as TextView
            val locationButton = row.getChildAt(3) as ImageButton

            rankView.text = (index + 1).toString()
            nameView.text = "Player ${index + 1}"
            scoreView.text = scores[index].toString()
            locationButton.setOnClickListener {
                showLocation("Player ${index + 1}")
            }
        }


        while (highScoreTable.childCount - 1 > scores.size) {
            highScoreTable.removeViewAt(highScoreTable.childCount - 1)
        }

        Log.d("HighScoreFragment", "Scores updated: $scores")
    }


    private fun initViews(v: View?) {

        loc_BTN_one.setOnClickListener { showLocation("Player 1") }
        loc_BTN_two.setOnClickListener { showLocation("Player 2") }
        loc_BTN_three.setOnClickListener { showLocation("Player 3") }
        loc_BTN_four.setOnClickListener { showLocation("Player 4") }
        loc_BTN_five.setOnClickListener { showLocation("Player 5") }
        loc_BTN_six.setOnClickListener { showLocation("Player 6") }
        loc_BTN_seven.setOnClickListener { showLocation("Player 7") }
        loc_BTN_eight.setOnClickListener { showLocation("Player 8") }
        loc_BTN_nine.setOnClickListener { showLocation("Player 9") }
        loc_BTN_ten.setOnClickListener { showLocation("Player 10") }


        playerNameOne.setText("Player 1")
        playerNameTwo.setText("Player 2")
        playerNameThree.setText("Player 3")
        playerNameFour.setText("Player 4")
        playerNameFive.setText("Player 5")
        playerNameSix.setText("Player 6")
        playerNameSeven.setText("Player 7")
        playerNameEight.setText("Player 8")
        playerNameNine.setText("Player 9")
        playerNameTen.setText("Player 10")

        playerScoreOne.setText("0")
        playerScoreTwo.setText("0")
        playerScoreThree.setText("0")
        playerScoreFour.setText("0")
        playerScoreFive.setText("0")
        playerScoreSix.setText("0")
        playerScoreSeven.setText("0")
        playerScoreEight.setText("0")
        playerScoreNine.setText("0")
        playerScoreTen.setText("0")
    }

    private fun showLocation(playerName: String) {

        Toast.makeText(requireContext(), "Showing location for $playerName", Toast.LENGTH_SHORT)
            .show()
        onLocationClicked?.onLocationClicked(playerName)
    }


    private fun findViews(v: View) {
        playerNameOne = v.findViewById(R.id.player_name_1)
        /*        playerNameTwo = v.findViewById(R.id.player_name_2)
                playerNameThree = v.findViewById(R.id.player_name_3)
                playerNameFour = v.findViewById(R.id.player_name_4)
                playerNameFive = v.findViewById(R.id.player_name_5)
                playerNameSix = v.findViewById(R.id.player_name_6)
                playerNameSeven = v.findViewById(R.id.player_name_7)
                playerNameEight = v.findViewById(R.id.player_name_8)
                playerNameNine = v.findViewById(R.id.player_name_9)
                playerNameTen = v.findViewById(R.id.player_name_10)*/
        playerScoreOne = v.findViewById(R.id.score_1)
        /*        playerScoreTwo = v.findViewById(R.id.score_2)
                playerScoreThree = v.findViewById(R.id.score_3)
                playerScoreFour = v.findViewById(R.id.score_4)
                playerScoreFive = v.findViewById(R.id.score_5)
                playerScoreSix = v.findViewById(R.id.score_6)
                playerScoreSeven = v.findViewById(R.id.score_7)
                playerScoreEight = v.findViewById(R.id.score_8)
                playerScoreNine = v.findViewById(R.id.score_9)
                playerScoreTen = v.findViewById(R.id.score_10)*/
        loc_BTN_one = v.findViewById(R.id.location_1)
        /*        loc_BTN_two = v.findViewById(R.id.location_2)
                loc_BTN_three = v.findViewById(R.id.location_3)
                loc_BTN_four = v.findViewById(R.id.location_4)
                loc_BTN_five = v.findViewById(R.id.location_5)
                loc_BTN_six = v.findViewById(R.id.location_6)
                loc_BTN_seven = v.findViewById(R.id.location_7)
                loc_BTN_eight = v.findViewById(R.id.location_8)
                loc_BTN_nine = v.findViewById(R.id.location_9)
                loc_BTN_ten = v.findViewById(R.id.location_10)*/
        highScoreTable = v.findViewById(R.id.score_table)
    }
}
