package com.example.hm_1

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hm_1.fragments.HighScoreFragment
import com.example.hm_1.Interface.HighScoreCallBack
import android.location.Location
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.os.Parcel
import android.os.Parcelable

data class Player(
    val name: String,
    val score: Int,
    val location: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(score)
        parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Player> {
        override fun createFromParcel(parcel: Parcel): Player {
            return Player(parcel)
        }

        override fun newArray(size: Int): Array<Player?> {
            return arrayOfNulls(size)
        }
    }
}

class GameOverActivity : AppCompatActivity() {

    private val players = mutableListOf<Player>()
    private lateinit var main_FRAME_list: FrameLayout
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        findViews()

        val score = intent?.getIntExtra("score", -1) ?: -1
        if (score == -1) {
            Log.e("GameOverActivity", "No score passed in Intent")
            finish()
            return
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation { latitude, longitude ->
            loadPlayers()

            val newPlayerName = "Player ${players.size + 1}"
            val locationString = "$latitude,$longitude"

            players.add(Player(newPlayerName, score, locationString))

            players.sortByDescending { it.score }

            savePlayers()

            val highScoreFragment = HighScoreFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("players", ArrayList(players))
            highScoreFragment.arguments = bundle

            highScoreFragment.onLocationClicked = object : HighScoreCallBack {
                override fun onLocationClicked(playerName: String) {
                    val player = players.find { it.name == playerName }
                    if (player != null) {
                        Log.d("GameOverActivity", "Player Name: $playerName, GPS Location: ${player.location}")
                    } else {
                        Log.d("GameOverActivity", "Player $playerName not found")
                    }
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.main_FRAME_list, highScoreFragment)
                .commit()
        }
    }

    private fun getCurrentLocation(callback: (Double, Double) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                callback(it.latitude, it.longitude)
            } ?: run {
                callback(0.0, 0.0) // Default location if unavailable
            }
        }
    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)
    }

    private fun savePlayers() {
        val sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val playersString = players.joinToString(";") {
            "${it.name},${it.score},${it.location}"
        }
        editor.putString("players", playersString)
        editor.apply()
    }

    private fun loadPlayers() {
        val sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE)
        val playersString = sharedPreferences.getString("players", "") ?: ""
        if (playersString.isNotEmpty()) {
            players.clear()
            players.addAll(playersString.split(";").map {
                val parts = it.split(",")
                if (parts.size >= 3) {
                    val name = parts[0]
                    val score = parts[1].toInt()
                    val location = parts.subList(2, parts.size).joinToString(",")
                    Player(name, score, location)
                } else {
                    Log.e("GameOverActivity", "Malformed player entry: $it")
                    null
                }
            }.filterNotNull())
        }
    }
}
