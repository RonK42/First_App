package com.example.hm_1.logic

import com.example.hm_1.model.Data_Manager

class Game_Manager {

    val dataManager = Data_Manager()
    val trunkRow = dataManager.getTrunkRow()
    val bowlingPinsMatrix = dataManager.getBowlingPinsMatrix()
    val lifeRow = dataManager.getLifeRow()

    fun initializeGame() {
        bowlingPinsMatrix[0][1].isAppear = true
        bowlingPinsMatrix[0][1].isPin = true
        bowlingPinsMatrix[0][3].isAppear = true
        bowlingPinsMatrix[0][3].isPin = false // This is a coin

        trunkRow[2] = true
    }

    fun movePinsOrCoinsOneRowDown(dataManager: Data_Manager) {
        val bowlingPinsMatrix = dataManager.getBowlingPinsMatrix()
        for (i in bowlingPinsMatrix.size - 1 downTo 0) {
            for (j in bowlingPinsMatrix[i].indices) {
                if (bowlingPinsMatrix[i][j].isAppear) {
                    if (i < bowlingPinsMatrix.size - 1) {
                        bowlingPinsMatrix[i + 1][j].isAppear = true
                        bowlingPinsMatrix[i + 1][j].isPin = bowlingPinsMatrix[i][j].isPin
                    }
                    bowlingPinsMatrix[i][j].isAppear = false
                    bowlingPinsMatrix[i][j].isPin = false
                }
            }
        }
    }

    fun randomAppearingPinOrCoin(dataManager: Data_Manager) {
        val bowlingPinsMatrix = dataManager.getBowlingPinsMatrix()
        val randomChance = (0..100).random()
        if (randomChance < 60) {
            val randomIndex = (0..4).random()
            bowlingPinsMatrix[0][randomIndex].isAppear = true
            bowlingPinsMatrix[0][randomIndex].isPin = randomChance < 30 // 50% chance for a pin
        }
    }

    fun moveTrunkLeftOrRight(dataManager: Data_Manager, direction: String) {
        for ((index, value) in trunkRow.withIndex()) {
            if (value && direction == "right" && index > 0) {
                trunkRow[index] = false
                trunkRow[index - 1] = true
                break
            }
            if (value && direction == "left" && index < 4) {
                trunkRow[index] = false
                trunkRow[index + 1] = true
                break
            }
        }
    }

    private fun removeLife(): Boolean {
        for (i in lifeRow.size - 1 downTo 0) {
            if (lifeRow[i]) {
                lifeRow[i] = false
                return true
            }
        }
        return false
    }

    fun checkIncident(gameManager: Game_Manager): Int {
        val bowlingPinsMatrix = gameManager.bowlingPinsMatrix
        val trunkRow = gameManager.trunkRow
        for (i in trunkRow.indices) {
            if (trunkRow[i] && bowlingPinsMatrix[bowlingPinsMatrix.size - 1][i].isAppear) {
                if (bowlingPinsMatrix[bowlingPinsMatrix.size - 1][i].isPin) {
                    // Remove life only once per collision
                    if (removeLife()) {
                        return 1
                    }
                } else {
                    // Handle coin collision
                    return 2
                }
            }
        }
        return 0
    }

}
