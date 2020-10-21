package com.izadinia.minesweeper

/**
 * This is a model class for the game board's general properties
 * Used in [BoardGenerator.generate] to generate a full game board.
 * @see [BoardGenerator] documents for more info
 */
data class Board(
    var rows: Int,
    var columns: Int,
    var noOfMines: Int,

    )