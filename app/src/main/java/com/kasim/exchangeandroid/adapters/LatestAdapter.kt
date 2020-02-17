package com.kasim.exchangeandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasim.exchangeandroid.R
import com.kasim.exchangeandroid.helpers.getCurrencies
import com.kasim.exchangeandroid.helpers.getDrawable
import com.kasim.exchangeandroid.models.Latest
import java.util.*

class LatestAdapter(
    internal var context: Context,
    internal var latest: Latest,
    internal var keys: ArrayList<String>
) : RecyclerView.Adapter<LatestAdapter.LatestViewHolder>() {


    val currencies = context.getCurrencies()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LatestAdapter.LatestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.latest_item, parent, false)
        return LatestViewHolder(view)
    }

    override fun onBindViewHolder(holder: LatestAdapter.LatestViewHolder, position: Int) {
        val values = currencies[keys[position]] as Map<String, String>
        holder.code.text = keys[position]
        holder.name.text = values["Name"]
        holder.flag.setImageResource(values["Image"]?.getDrawable(context)!!)
        holder.money.text = values["Symbol"] + "%.3f".format(latest.rates!!.get(keys[position]) ?: 1.0)
    }

    override fun getItemCount(): Int {
        if (latest == null) return 0
        else return keys.size
    }

    inner class LatestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var name: TextView
        internal var code: TextView
        internal var money: TextView
        internal var flag: ImageView

        init {
            name = itemView.findViewById(R.id.name)
            code = itemView.findViewById(R.id.code)
            money = itemView.findViewById(R.id.money)
            flag = itemView.findViewById(R.id.flag)

        }
    }

}
