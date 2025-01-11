package com.example.hm_1

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.hm_1.Utilities.Constants
import com.example.hm_1.Utilities.SignalManager
import com.example.hm_1.logic.Game_Manager
import com.example.hm_1.model.Data_Manager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    lateinit var Distance_LBL : MaterialTextView
    lateinit var Main_Button_Left: ExtendedFloatingActionButton
    lateinit var Main_Button_Right: ExtendedFloatingActionButton
    lateinit var Cars: Array<AppCompatImageView>
    lateinit var Pins: Array<Array<AppCompatImageView>>
    lateinit var license_Row: Array<AppCompatImageView>
    lateinit var gameManager: Game_Manager
    private var startTime: Long = 0
    private var gameOn: Boolean = false
    private lateinit var gameJob: Job
    private lateinit var mediaPlayer: MediaPlayer
    private var Distance = 0

    private fun updateGameUi() {
        val currentTime = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        gameManager = Game_Manager()
        gameManager.initializeGame()
        initViews()
        refreshLicensePanel()
        initListeners()
        onResume()
    }

    override fun onPause() {
        super.onPause()
        if (::gameJob.isInitialized && gameJob.isActive) {
            gameJob.cancel() // Stop the game loop
        }
        gameOn = false

        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!::mediaPlayer.isInitialized) {
            mediaPlayer = MediaPlayer.create(this, R.raw.soundtrack)
            mediaPlayer.isLooping = true
        }
        mediaPlayer.start()
        if (!gameOn) {

            gameOn = true
            startTime = System.currentTimeMillis()
            gameJob = lifecycleScope.launch {
                while (gameOn) {
                    Distance += 10
                    gameManager.movePinsOneRowDown(gameManager.dataManager)
                    gameManager.randomAppearingPin(gameManager.dataManager)
                    if (gameManager.checkIncident(gameManager)) {
                        SignalManager(this@MainActivity).toast("BE MORE CAREFUL!!!")
                        SignalManager(this@MainActivity).vibrate(500)
                    }
                    if (!gameManager.lifeRow[0]) {
                        gameOn = false
                        break
                    }
                    refreshUI()
                    delay(Constants.Game.DELAY)
                }
                changeActivity()
                SignalManager(this@MainActivity).vibrate(1500)
                SignalManager(this@MainActivity).toast("GAME OVER!!!!!")
            }
        }
    }

    private fun initViews() {
        refreshPinsLoc()
        refreshTruckLoc()
        refrashDistance()

    }

    private fun refrashDistance() {
        Distance_LBL.visibility = View.VISIBLE
        Distance_LBL.text = "Distance: $Distance"
    }

    private fun refreshLicensePanel() {
        val licensePanel = gameManager.lifeRow
        for ((panelIndex, panel) in license_Row.withIndex()) {
            if (licensePanel[panelIndex]) {
                license_Row[panelIndex].visibility = View.VISIBLE
            } else {
                license_Row[panelIndex].visibility = View.INVISIBLE
            }
        }
    }

    private fun refreshPinsLoc() {
        val pinMatrix = gameManager.bowlingPinsMatrix
        //move the trunk

        for ((rowIndex, pinRow) in Pins.withIndex()) {
            for ((pinIndex, pin) in Pins[rowIndex].withIndex()) {
                if (pinMatrix[rowIndex][pinIndex]) {
                    Pins[rowIndex][pinIndex].visibility = View.VISIBLE
                } else {
                    Pins[rowIndex][pinIndex].visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun refreshTruckLoc() {
        val trunkRow = gameManager.trunkRow
        for ((index, isVisible) in trunkRow.withIndex()) {
            if (isVisible) {
                Cars[index].visibility = View.VISIBLE
            } else {
                Cars[index].visibility = View.INVISIBLE
            }
        }
    }

    private fun initListeners() {
        Main_Button_Left.setOnClickListener { view: View ->
            truckGoLeft()
        }
        Main_Button_Right.setOnClickListener { view: View ->
            truckGoRight()
        }
    }

    private fun truckGoLeft() {
        gameManager.moveTrunkLeftOrRight(gameManager.dataManager, "right")
        refreshTruckLoc()
    }

    private fun truckGoRight() {
        gameManager.moveTrunkLeftOrRight(gameManager.dataManager, "left")
        refreshTruckLoc()
    }


    private fun findViews() {
        Distance_LBL = findViewById(R.id.main_LBL_distance)
        Main_Button_Left = findViewById(R.id.Main_Button_Left)
        Main_Button_Right = findViewById(R.id.Main_Button_Right)
        Cars = arrayOf(
            findViewById(R.id.Player_One),
            findViewById(R.id.Player_Two),
            findViewById(R.id.Player_Three),
            findViewById(R.id.Player_Four),
            findViewById(R.id.Player_Five)
        )
        Pins = arrayOf(
            arrayOf(
                findViewById(R.id.One_one),
                findViewById(R.id.One_two),
                findViewById(R.id.One_three),
                findViewById(R.id.One_Four),
                findViewById(R.id.One_Five)

            ),
            arrayOf(
                findViewById(R.id.Two_One),
                findViewById(R.id.Two_Two),
                findViewById(R.id.Two_Three),
                findViewById(R.id.Two_Four),
                findViewById(R.id.Two_Five)

            ),
            arrayOf(
                findViewById(R.id.Three_One),
                findViewById(R.id.Three_Two),
                findViewById(R.id.Three_Three),
                findViewById(R.id.Three_Four),
                findViewById(R.id.Three_Five)
            ),
        )
        license_Row = arrayOf(
            findViewById(R.id.main_IMG_license1),
            findViewById(R.id.main_IMG_license2),
            findViewById(R.id.main_IMG_license3),
        )
    }

    private fun refreshUI() {
        refrashDistance()
        refreshTruckLoc()
        refreshPinsLoc()
        refreshLicensePanel()
    }

    private fun changeActivity() {
        val intent = Intent(this, gameOverActivity::class.java)
        startActivity(intent)
    }


}
