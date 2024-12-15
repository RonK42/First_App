package com.example.hm_1

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
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
        startGame()
    }


    private fun initViews() {
        refreshPinsLoc()
        refreshTruckLoc()
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

    private fun startGame() {
        mediaPlayer = MediaPlayer.create(this, R.raw.soundtrack)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        var isVib = false
        if (!gameOn) {
            gameOn = true
            startTime = System.currentTimeMillis()
            gameJob = lifecycleScope.launch {
                while (gameOn) {

                    gameManager.movePinsOneRowDown(gameManager.dataManager)
                    gameManager.randomAppearingPin(gameManager.dataManager)
                    isVib = gameManager.checkIncident(gameManager)
                    if (isVib) {

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



    private fun findViews() {
        Main_Button_Left = findViewById(R.id.Main_Button_Left)
        Main_Button_Right = findViewById(R.id.Main_Button_Right)
        Cars = arrayOf(
            findViewById(R.id.Car_Left),
            findViewById(R.id.Car_Center),
            findViewById(R.id.Car_Right)
        )
        Pins = arrayOf(
            arrayOf(
                findViewById(R.id.Pin_Top_Left),
                findViewById(R.id.Pin_Top_Center),
                findViewById(R.id.Pin_Top_Right)
            ),
            arrayOf(
                findViewById(R.id.Pin_Center_Left),
                findViewById(R.id.Pin_Center_Center),
                findViewById(R.id.Pin_Center_Right)

            ),
            arrayOf(
                findViewById(R.id.Pin_Bottom_Left),
                findViewById(R.id.Pin_Bottom_Center),
                findViewById(R.id.Pin_Bottom_Right)
            ),
        )
        license_Row = arrayOf(
            findViewById(R.id.main_IMG_license1),
            findViewById(R.id.main_IMG_license2),
            findViewById(R.id.main_IMG_license3),


            )


    }

    private fun refreshUI() {
        refreshTruckLoc()
        refreshPinsLoc()
        refreshLicensePanel()
    }

    private fun changeActivity() {
        val intent = Intent(this, gameOverActivity::class.java)
        startActivity(intent)
    }


}
