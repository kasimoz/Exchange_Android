package com.kasim.exchangeandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasim.exchangeandroid.R
import com.kasim.exchangeandroid.helpers.ItemType
import com.kasim.exchangeandroid.helpers.SharedPreferenceManager
import com.kasim.exchangeandroid.helpers.getDrawable
import com.kasim.exchangeandroid.models.Settings


class SettingsAdapter(
    internal var context: Context,
    internal var settings: Settings,
    internal var listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(selected: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ItemType.Header.value) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.settings_header_item, parent, false)
            return HeaderViewHolder(view)
        } else if (viewType == ItemType.Item.value) {
            val view = LayoutInflater.from(context).inflate(R.layout.settings_item, parent, false)
            return ItemViewHolder(view)
        } else {
            return ItemViewHolder(View(context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            val headerViewHolder = holder as? HeaderViewHolder
            headerViewHolder?.title?.text = settings.list.get(position).title
        } else if (holder is ItemViewHolder) {
            val itemViewHolder = holder as? ItemViewHolder
            itemViewHolder?.title?.text = settings.list.get(position).title
            when (settings.list.get(position).title) {
                "Currency" -> itemViewHolder?.value?.text =
                    SharedPreferenceManager().getBaseCurrency(context).get(0)
                "From" -> itemViewHolder?.value?.text =
                    SharedPreferenceManager().getDefaultFrom(context)
                "To" -> itemViewHolder?.value?.text =
                    SharedPreferenceManager().getDefaultTo(context)
                "Widget Base" -> itemViewHolder?.value?.text =
                    SharedPreferenceManager().getWidgetBase(context)
                "Widget First" -> itemViewHolder?.value?.text =
                    SharedPreferenceManager().getWidgetFirst(context)
                "Widget Second" -> itemViewHolder?.value?.text =
                    SharedPreferenceManager().getWidgetSecond(context)
                else -> itemViewHolder?.value?.text = ""
            }
            itemViewHolder?.icon?.setImageResource(
                settings.list.get(position).image?.getDrawable(
                    context
                )!!
            )
        }
    }

    override fun getItemCount(): Int {
        return settings.list.size
    }


    override fun getItemViewType(position: Int): Int {
        return if (settings.list.get(position).header) {
            ItemType.Header.value
        } else ItemType.Item.value
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var title: TextView

        init {
            title = itemView.findViewById(R.id.title)
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        override fun onClick(v: View?) {
            listener.onItemClick(settings.list.get(adapterPosition).title)
        }

        internal var title: TextView
        internal var value: TextView
        internal var icon: ImageView

        init {
            title = itemView.findViewById(R.id.title)
            value = itemView.findViewById(R.id.value)
            icon = itemView.findViewById(R.id.icon)
            itemView.setOnClickListener(this)
        }
    }

}


