package com.izadinia.minesweeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class GameOverDialog : DialogFragment() {
    private lateinit var conditionTxt: TextView
    private lateinit var resetBtn: Button

    companion object {
        fun newInstance(gameCondition: String): GameOverDialog {
            val dialog = GameOverDialog()
            val args = Bundle()
            args.putString("condition", gameCondition)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_gameover_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        conditionTxt = view.findViewById(R.id.gameCondition)
        resetBtn = view.findViewById(R.id.restartBtn)
        conditionTxt.text = arguments?.getString("condition", "You Lost!")
    }
}