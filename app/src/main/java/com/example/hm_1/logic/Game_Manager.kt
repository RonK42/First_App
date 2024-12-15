package com.example.hm_1.logic

import android.util.Log
import android.view.View
import com.example.hm_1.model.Data_Manager


class Game_Manager {

    val dataManager = Data_Manager()
    val trunkRow = dataManager.getTrunkRow()
    val bowlingPinsMatrix = dataManager.getBowlingPinsMatrix()
    val lifeRow = dataManager.getLifeRow()

    fun initializeGame() {
        // Set the default state of the pins
        bowlingPinsMatrix[0][0] = true

        // Set the default state of the trunk
        trunkRow[0] = true

    }

    fun movePinsOneRowDown(dataManager: Data_Manager) {
        val bowlingPinsMatrix = dataManager.getBowlingPinsMatrix()

        for (i in bowlingPinsMatrix.size - 1 downTo 1) {
            for (j in bowlingPinsMatrix[i].indices) {
                bowlingPinsMatrix[i][j] = bowlingPinsMatrix[i - 1][j]
            }
        }
        for (j in bowlingPinsMatrix[0].indices) {
            bowlingPinsMatrix[0][j] = false
        }
    }

    fun randomAppearingPin(dataManager: Data_Manager) {

        val bowlingPinsMatrix = dataManager.getBowlingPinsMatrix()
        val randomChance = (0..100).random()//Korean Random
        if (randomChance < 50) {
            val randomIndex = (0..2).random()
            bowlingPinsMatrix[0][randomIndex] = true
        }
    }


    public fun moveTrunkLeftOrRight(dataManager: Data_Manager, direction: String) {
        for ((index, value) in trunkRow.withIndex()) {
            if (value && direction == "right" && index > 0) {
                trunkRow[index] = false
                trunkRow[index - 1] = true
                break
            }
            if (value && direction == "left" && index < 2) {
                trunkRow[index] = false
                trunkRow[index + 1] = true
                break
            }
        }


    }

    fun removeLife(): Boolean {
        val lifeRow = dataManager.getLifeRow()
        for (i in lifeRow.size - 1 downTo 0) {
            if (lifeRow[i]) {
                lifeRow[i] = false
                return true
            }
        }
        return false
    }

    fun howManyHeartLeft(dataManager: Data_Manager): Int {
        var count = 0
        val lifeRow = dataManager.getLifeRow()
        for (i in lifeRow.indices) {
            if (lifeRow[i]) {
                count++
            }
        }
        return count
    }

    fun checkIncident(gameManager: Game_Manager): Boolean {
        val bowlingPinsMatrix = gameManager.bowlingPinsMatrix
        val trunkRow = gameManager.trunkRow
        for (i in trunkRow.indices) {
            if (trunkRow[i] && bowlingPinsMatrix[3][i]) {
                removeLife()
                return true
            }
        }
        return false
    }

}





