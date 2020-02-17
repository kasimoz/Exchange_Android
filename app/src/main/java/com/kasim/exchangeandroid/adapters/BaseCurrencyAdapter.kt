package com.kasim.exchangeandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasim.exchangeandroid.helpers.getCurrencies
import com.kasim.exchangeandroid.helpers.getKeys


class BaseCurrencyAdapter(
    internal var context: Context,
    internal var listener: OnItemClickListener
) : RecyclerView.Adapter<BaseCurrencyAdapter.BaseCurrencyViewHolder>() {

    val keys = context.getCurrencies().getKeys()

    interface OnItemClickListener {
        fun onItemClick(selected: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseCurrencyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(com.kasim.exchangeandroid.R.layout.base_item, parent, false)
        return BaseCurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseCurrencyViewHolder, position: Int) {
        holder.code.text = keys[position]
    }

    override fun getItemCount(): Int {
        return keys.size
    }

    inner class BaseCurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        override fun onClick(v: View?) {
            listener.onItemClick(keys.get(adapterPosition))
        }

        internal var code: TextView

        init {
            code = itemView.findViewById(com.kasim.exchangeandroid.R.id.code)
            itemView.setOnClickListener(this)
        }
    }

}
