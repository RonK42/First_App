package com.example.hm_1.model

data class Cell(
    var isAppear: Boolean,
    var isPin : Boolean
)

class Data_Manager {

    private val bowlingPinsMatrix: Array<Array<Cell>> =
        Array(4) { Array(5) { Cell(false, false) } }

    private val trunkRow: Array<Boolean> =
        Array(5) { false }

    private val lifeRow: Array<Boolean> =
        Array(3) { true }

    fun getLifeRow(): Array<Boolean> {
        return lifeRow
    }

    fun getBowlingPinsMatrix(): Array<Array<Cell>> {
        return bowlingPinsMatrix
    }

    fun getTrunkRow(): Array<Boolean> {
        return trunkRow
    }
}