package com.izadinia.minesweeper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class OurRecyclerGenerator(
    private val cells: Array<Slot>,
    private val clickListener: (Int, SlotViewHolder) -> Unit,
    private val longClick: (Int, SlotViewHolder) -> Boolean
) :
    RecyclerView.Adapter<SlotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        return SlotViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.slots_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        holder.bind(cells[position], position, holder, clickListener, longClick)
    }

    override fun getItemCount(): Int {
        return cells.size
    }
}

class SlotViewHolder(eachSlot: View) : RecyclerView.ViewHolder(eachSlot) {
    val minesNumber: TextView = eachSlot.findViewById(R.id.mineNum)
    val card: CardView = eachSlot.findViewById(R.id.card)
    fun bind(
        slot: Slot, position: Int, holder: SlotViewHolder,
        clickListener: (Int, SlotViewHolder) -> Unit,
        longClick: (Int, SlotViewHolder) -> Boolean
    ) {
        card.setOnClickListener { clickListener(position, holder) }
        card.setOnLongClickListener { longClick(position, holder) }
    }
}