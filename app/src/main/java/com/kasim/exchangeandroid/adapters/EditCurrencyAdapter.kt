package com.kasim.exchangeandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasim.exchangeandroid.helpers.getCurrencies
import com.kasim.exchangeandroid.helpers.getDrawable
import com.kasim.exchangeandroid.helpers.getKeys
import java.util.*


class EditCurrencyAdapter(
    internal var context: Context,
    internal var listCheck: ArrayList<String>,
    internal var listener: OnItemClickListener
) : RecyclerView.Adapter<EditCurrencyAdapter.EditCurrencyViewHolder>() {

    val keys = context.getCurrencies().getKeys()
    val currencies = context.getCurrencies()

    interface OnItemClickListener {
        fun onItemClick(selected: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditCurrencyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(com.kasim.exchangeandroid.R.layout.edit_item, parent, false)
        return EditCurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditCurrencyViewHolder, position: Int) {
        val values = currencies[keys[position]] as Map<String, String>
        holder.code.text = keys[position]
        holder.name.text = values["Name"]
        holder.flag.setImageResource(values["Image"]?.getDrawable(context)!!)
        if (listCheck.contains(keys[position])) {
            holder.check.visibility = View.VISIBLE
        } else {
            holder.check.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return keys.size
    }

    inner class EditCurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        override fun onClick(v: View?) {
            listener.onItemClick(keys.get(adapterPosition))
        }

        internal var name: TextView
        internal var code: TextView
        internal var flag: ImageView
        internal var check: ImageView

        init {
            name = itemView.findViewById(com.kasim.exchangeandroid.R.id.name)
            code = itemView.findViewById(com.kasim.exchangeandroid.R.id.code)
            flag = itemView.findViewById(com.kasim.exchangeandroid.R.id.flag)
            check = itemView.findViewById(com.kasim.exchangeandroid.R.id.check)
            itemView.setOnClickListener(this)
        }


    }

}
