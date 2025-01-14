package com.example.hm_1

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hm_1.Interface.TiltCallback
import com.example.hm_1.Utilities.Constants
import com.example.hm_1.Utilities.SignalManager
import com.example.hm_1.Utilities.TiltDetector
import com.example.hm_1.logic.Game_Manager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tiltDetector: TiltDetector
    private lateinit var gameManager: Game_Manager
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var gameJob: Job

    private var tiltMode: Boolean = false
    private var difficulty: String = "EASY"
    private var gameOn: Boolean = false
    private var distance: Int = 0
    private var score: Int = 0
    private lateinit var scoreLebel: MaterialTextView
    private lateinit var distanceLabel: MaterialTextView
    private lateinit var leftButton: ExtendedFloatingActionButton
    private lateinit var rightButton: ExtendedFloatingActionButton
    private lateinit var cars: Array<View>
    private lateinit var pins: Array<Array<View>>
    private lateinit var licenseRow: Array<View>

    private val coinIcon: Int = R.drawable.goldbitcoin
    private val pinIcon: Int = R.drawable.bowling_pin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Determine game mode and difficulty
        val mode = intent.getStringExtra("MODE")
        tiltMode = mode == "TILT_MODE"
        difficulty = intent.getStringExtra("DIFFICULTY") ?: "EASY"

        // Initialize views and game manager
        findViews()
        gameManager = Game_Manager().apply { initializeGame() }

        initViews()

        if (tiltMode) initTiltMode() else initButtonMode()
    }

    override fun onResume() {
        super.onResume()
        setupMediaPlayer()
        startGameLoop()
    }

    override fun onPause() {
        super.onPause()
        stopGameLoop()
        pauseMediaPlayer()
    }

    private fun findViews() {
        scoreLebel = findViewById(R.id.main_LBL_score)
        distanceLabel = findViewById(R.id.main_LBL_distance)
        leftButton = findViewById(R.id.Main_Button_Left)
        rightButton = findViewById(R.id.Main_Button_Right)

        cars = arrayOf(
            findViewById(R.id.Player_One),
            findViewById(R.id.Player_Two),
            findViewById(R.id.Player_Three),
            findViewById(R.id.Player_Four),
            findViewById(R.id.Player_Five)
        )

        pins = arrayOf(
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
            )
        )

        licenseRow = arrayOf(
            findViewById(R.id.main_IMG_license1),
            findViewById(R.id.main_IMG_license2),
            findViewById(R.id.main_IMG_license3)
        )
    }

    private fun initViews() {
        refreshUI()
    }

    private fun initTiltMode() {
        leftButton.visibility = View.INVISIBLE
        rightButton.visibility = View.INVISIBLE
        tiltDetector = TiltDetector(this, object : TiltCallback {
            override fun tiltX() {
                handleTilt(tiltDetector.tiltCounterX)
            }

            override fun tiltY() {}
        })
        tiltDetector.start()
    }

    private fun initButtonMode() {
        leftButton.visibility = View.VISIBLE
        rightButton.visibility = View.VISIBLE
        leftButton.setOnClickListener { moveTruckRight() }
        rightButton.setOnClickListener { moveTruckLeft() }
    }

    private fun handleTilt(currentTilt: Int) {
        if (currentTilt > 0) moveTruckLeft() else moveTruckRight()
    }

    private fun moveTruckLeft() {
        gameManager.moveTrunkLeftOrRight(gameManager.dataManager, "left")
        refreshTruckLocation()
    }

    private fun moveTruckRight() {
        gameManager.moveTrunkLeftOrRight(gameManager.dataManager, "right")
        refreshTruckLocation()
    }

    private fun startGameLoop() {
        if (gameOn) return

        gameOn = true
        val gameDelay = if (difficulty == "EASY") Constants.Game.DELAY else Constants.Game.DELAY / 2

        gameJob = lifecycleScope.launch {
            while (gameOn) {
                distance += 10
                gameManager.movePinsOrCoinsOneRowDown(gameManager.dataManager)
                gameManager.randomAppearingPinOrCoin(gameManager.dataManager) // Add coins/pins dynamically
                if (gameManager.checkIncident(gameManager)==1) {
                    SignalManager(this@MainActivity).toast("BE MORE CAREFUL!!!")
                    SignalManager(this@MainActivity).vibrate(700)
                }
                if (gameManager.checkIncident(gameManager)==2){
                    SignalManager(this@MainActivity).vibrate(200)
                    addScore()
                }
                if (!gameManager.lifeRow[0]) {
                    gameOn = false
                    break
                }
                refreshUI()
                delay(gameDelay)
            }
            endGame()
        }
    }

    private fun addScore() {
        score += 10
        refreshScore()
    }
    private fun stopGameLoop() {
        if (::gameJob.isInitialized && gameJob.isActive) gameJob.cancel()
        gameOn = false
    }

    private fun refreshUI() {
        refreshScore()
        refreshDistance()
        refreshTruckLocation()
        refreshPinsLocation()
        refreshLicensePanel()
    }

    private fun refreshScore() {
        scoreLebel.text = "Score: $score"
    }
    private fun refreshDistance() {
        distanceLabel.text = "Distance: $distance"
    }

    private fun refreshTruckLocation() {
        val trunkRow = gameManager.trunkRow
        cars.forEachIndexed { index, car ->
            car.visibility = if (trunkRow[index]) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun refreshPinsLocation() {
        val pinMatrix = gameManager.bowlingPinsMatrix
        pins.forEachIndexed { rowIndex, pinRow ->
            pinRow.forEachIndexed { pinIndex, pin ->
                val pinData = pinMatrix[rowIndex][pinIndex]
                if (pinData.isAppear) {
                    pin.visibility = View.VISIBLE
                    if (!pinData.isPin) {
                        (pin as? ImageView)?.setImageResource(coinIcon) // Cast pin to ImageView
                    } else {
                        (pin as? ImageView)?.setImageResource(pinIcon) // Cast pin to ImageView
                    }
                } else {
                    pin.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun refreshLicensePanel() {
        val licensePanel = gameManager.lifeRow
        licenseRow.forEachIndexed { index, license ->
            license.visibility = if (licensePanel[index]) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun setupMediaPlayer() {
        if (!::mediaPlayer.isInitialized) {
            mediaPlayer = MediaPlayer.create(this, R.raw.soundtrack)
            mediaPlayer.isLooping = true
        }
        mediaPlayer.start()
    }

    private fun pauseMediaPlayer() {
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    private fun endGame() {
        SignalManager(this).toast("GAME OVER!!!!!")
        SignalManager(this).vibrate(1500)
        val intent = Intent(this, gameOverActivity::class.java)
        startActivity(intent)
        finish()
    }
}