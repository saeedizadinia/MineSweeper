package com.izadinia.minesweeper

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

enum class Type {
    EMPTY,
    NUMBER,
    MINE
}

