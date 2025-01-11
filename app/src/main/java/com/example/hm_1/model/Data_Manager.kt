package com.example.hm_1.model

class Data_Manager {

    //Matrix to store the default state of the pins+trunk

    private val bowlingPinsMatrix: Array<Array<Boolean>> =
        Array(4) { Array(5) { false } }

    private val trunkRow: Array<Boolean> =
        Array(5) { false }

    private val lifeRow: Array<Boolean> =
        Array(3) { true }

    fun getLifeRow(): Array<Boolean> {
        return lifeRow
    }
    fun getBowlingPinsMatrix(): Array<Array<Boolean>> {
        return bowlingPinsMatrix
    }

    // Method to get the trunk matrix
    fun getTrunkRow(): Array<Boolean> {
        return trunkRow
    }
}