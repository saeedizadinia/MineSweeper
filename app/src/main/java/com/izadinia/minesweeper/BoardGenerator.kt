package com.izadinia.minesweeper

import kotlin.random.Random

/**
 * included in this class, are all the tools needed to build the game board.
 * the [mineNo] value is later used to check for winning condition and is the way
 * to make sure the right amount of mines generated is calculated. that means
 * the value of (Rows*Cols)-mineNo is the amount of non-mine slots, and whenever
 * the player clicks on a non-mine slot, it's value gets decreased by 1,
 *  when it's value becomes zero, the player have won the game.
 *  this logic is implemented in [MainActivity].
 *
 *  @author Saeed Izadinia
 */
class BoardGenerator {
    private var slots = arrayOf<Array<Slot>>()
    private var size = 0
    private lateinit var mBoard: Board
    var mineNo = 0

    /**
     * this function generates the board using a 2D array and finally turns this matrix
     * into a simple array to be easily used as an array of [Slot] objects
     * to populate the RecyclerView or a ListView. (@see [setRecyclerCells])
     *
     * @param [board] of the Type [Board]
     * @return an [Array] of [Slot] objects
     */
    fun generate(board: Board): Array<Slot> {
        this.mBoard = board
        size = mBoard.columns * mBoard.rows
        for (i in 0 until mBoard.rows) {
            var array = arrayOf<Slot>()
            for (j in 0 until mBoard.columns) {
                array += Slot(Type.EMPTY)
            }
            slots += array
        }
        generateMines()
        for (row in 0 until mBoard.rows) {
            for (col in 0 until mBoard.columns)
                if (slots[row][col].type() == Type.MINE && !slots[row][col].isMineChecked) {
                    generateNumbers(row, col)
                }
        }

        return setRecyclerCells(slots)
    }

    /**
     * this function turn a 2d array into a 1d array to be used as the array of
     * objects in a RecyclerView or a ListView.
     */
    private fun setRecyclerCells(cells: Array<Array<Slot>>): Array<Slot> {
        val recyclerCells = Array(size) { Slot(Type.EMPTY) }
        var i = 0
        val m = recyclerCells.size
        for (array in cells) {
            for (value in array) {
                recyclerCells[i] = value
                i++
            }
        }
        return recyclerCells
    }

    private fun generateMines() {
        for (m in 1..mBoard.noOfMines) {
            var randRow = Random.nextInt(0, mBoard.rows - 1)
            var randCol = Random.nextInt(0, mBoard.columns - 1)
            if (slots[randRow][randCol].type() != Type.MINE) {
                slots[randRow][randCol] = Slot(Type.MINE)
                mineNo++

            } else {
                randRow = Random.nextInt(0, mBoard.rows - 1)
                randCol = Random.nextInt(0, mBoard.columns - 1)
                if (slots[randRow][randCol].type() != Type.MINE) {
                    slots[randRow][randCol] = Slot(Type.MINE)
                    mineNo++
                }
            }
        }

    }

    private fun generateNumbers(row: Int, col: Int) {
        if (!slots[row][col].isMineChecked)
            slots[row][col].isMineChecked = true
        else
            return
        for (i in row - 1..row + 1) {
            for (j in col - 1..col + 1) {
                if (i < 0 || j < 0 || i >= mBoard.rows || j >= mBoard.columns)
                    continue
                else if (i == row && j == col)
                    continue
                else {
                    if (slots[i][j].type() != Type.MINE) {
                        slots[i][j].numberValue += 1
                        slots[i][j].setType(Type.NUMBER)
                    } else {
                        if (slots[i][j].isMineChecked)
                            continue
                        else
                            generateNumbers(i, j)
                    }
                }
            }
        }
    }
}