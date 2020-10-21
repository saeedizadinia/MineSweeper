package com.izadinia.minesweeper

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * all the UI necessary logic and changes happen here.
 *
 * @author Saeed Izadinia
 */

class MainActivity : AppCompatActivity() {

    companion object {
        //change these numbers to get the desired board size and number of mines
        const val ROWS = 8
        const val COLS = 8
        private const val MINES = 12
    }

    private var remainingCells = 0
    private lateinit var dialog: GameOverDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var sHolder: SlotViewHolder
    private lateinit var adapter: OurRecyclerGenerator
    private var board = arrayOf<Slot>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler)
        initializeBoard()
    }

    private fun initializeBoard() {
        val glm = GridLayoutManager(this, COLS)
        recyclerView.layoutManager = glm
        val generator = BoardGenerator()
        board = generator.generate(Board(ROWS, COLS, MINES))
        if (generator.mineNo != 0)
            remainingCells = (ROWS * COLS) - generator.mineNo
        adapter = OurRecyclerGenerator(board, { position: Int, holder: SlotViewHolder ->
            clickListener(
                position,
                holder
            )
        }, { pos: Int, hold: SlotViewHolder ->
            longClick(
                pos,
                hold
            )
        })
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
    }

    /**
     * the long click logic to set or unset flags on each cell happens here.
     * previously it's been passed onto MainActivity
     * from [OurRecyclerGenerator] adapter class with necessary params.
     *
     * @param pos the index at which the user long clicked and [hold] the corresponding
     * [RecyclerView.ViewHolder] of the cell user long clicked.
     */

    private fun longClick(pos: Int, hold: SlotViewHolder): Boolean {
        if (!board[pos].isFlagged && !board[pos].isRevealed) {
            board[pos].isFlagged = true
            flagCell("F", hold, true)
        } else if (board[pos].isFlagged && !board[pos].isRevealed) {
            board[pos].isFlagged = false
            flagCell("", hold, false)
        }
        return true
    }

    private fun flagCell(text: String, holder: SlotViewHolder, isVisible: Boolean) {
        if (isVisible) {
            holder.minesNumber.text = text
            holder.minesNumber.setTextColor(resources.getColor(R.color.white))
            holder.minesNumber.visibility = View.VISIBLE
        } else {
            holder.minesNumber.text = ""
            holder.minesNumber.setTextColor(resources.getColor(R.color.colorPrimary))
            holder.minesNumber.visibility = View.GONE
        }
    }

    /**
     * @see longClick the description is pretty much the same.
     */
    private fun clickListener(position: Int, holder: SlotViewHolder) {
        sHolder = holder
        if (!board[position].isFlagged && !board[position].isRevealed) {
            when (board[position].type()) {
                Type.MINE -> {
                    showGameOverDialog("You Lost!")
                }
                Type.NUMBER -> {
                    changeUI(board[position].numberValue.toString(), holder)
                    if (remainingCells == 0) {
                        showGameOverDialog("You Win!")
                    }
                }
                Type.EMPTY -> {
                    changeUI("", holder)
                    revealNeighborEmptyCells(position)
                    if (remainingCells == 0) {
                        showGameOverDialog("You Win!")
                    }
                }
            }
        }
    }

    /**
     * when the user clicks on an empty cell the game should find all of
     * its surrounding non-mine cells. that is the responsibility of this Recursive function
     *
     * @param position the cell index at which the function is currently working on
     */

    private fun revealNeighborEmptyCells(position: Int) {
        val row = matrixRow(position)
        val col = matrixCol(position)
        if (!board[position].isEmptyChecked)
            board[position].isEmptyChecked = true
        for (i in row - 1..row + 1) {
            for (j in col - 1..col + 1) {
                val index = arrayIndex(i, j)
                if (i < 0 || j < 0 || i >= ROWS || j >= COLS)
                    continue
                else if (i == row && j == col)
                    continue
                else if (board[index].isEmptyChecked)
                    continue
                else {
                    if (board[index].type() == Type.EMPTY &&
                        !board[index].isEmptyChecked
                    ) {
                        val hold = SlotViewHolder(recyclerView[index])
                        changeUI("", hold)
                        board[index].isRevealed = true
                        //board[index].isEmptyChecked = true
                        revealNeighborEmptyCells(index)
                    } else if (board[index].type() == Type.NUMBER && !board[index].isRevealed) {
                        val hold = SlotViewHolder(recyclerView[index])
                        changeUI(
                            board[index].numberValue.toString(),
                            hold
                        )
                        board[index].isRevealed = true
                        //board[index].isEmptyChecked = true
                    } else if (board[index].type() == Type.MINE)
                        continue
                }
            }
        }
    }

    /**
     * gets the coordination of a matrix and changes it to the exact
     * corresponding index of it in a simple array.
     */
    private fun arrayIndex(row: Int, col: Int): Int {
        return (row * COLS) + col
    }

    /**
     * gets the index of an array and returns the row index of its corresponding matrix
     */
    private fun matrixRow(index: Int): Int {
        return index / COLS
    }

    /**
     * gets the index of an array and returns the column index of its corresponding matrix
     */
    private fun matrixCol(index: Int): Int {
        return index % COLS
    }

    /**
     * this function is responsible for most common in game UI changes
     */
    private fun changeUI(cardText: String?, holder: SlotViewHolder) {
        remainingCells--
        holder.card.setBackgroundColor(resources.getColor(R.color.white))
        holder.minesNumber.text = cardText
        holder.minesNumber.visibility = View.VISIBLE
    }

    private fun showGameOverDialog(condition: String) {
        dialog = GameOverDialog.newInstance(condition)
        val fm = supportFragmentManager
        dialog.isCancelable = false
        dialog.show(fm, "dialog_game_over")
    }

    /**
     * the onClick function of the restart button. (declared in XML file "dialog_gameover_layout")
     */
    fun onResetClicked(view: View) {
        recyclerView.removeAllViews()
        recyclerView.adapter = null
        recyclerView.layoutManager = null
        initializeBoard()
        dialog.dismiss()
    }
}