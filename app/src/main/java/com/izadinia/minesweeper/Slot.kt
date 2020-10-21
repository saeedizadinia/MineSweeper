package com.izadinia.minesweeper

/**
 * each cell on the [Board] is of the type [Slot]
 * this class holds important info about each cell and it must have one of the three [Type]s.
 */
class Slot(private var type: Type) {

    var isFlagged = false
    var isRevealed = false
    var isMineChecked = false
    var isEmptyChecked = false
    var numberValue: Int = 0

    fun type(): Type {
        return this.type
    }

    fun setType(type: Type) {
        this.type = type
    }
}

/**
 * all possible types of cells is declared here
 */

enum class Type {
    EMPTY,
    NUMBER,
    MINE
}

