package com.kasim.exchangeandroid.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kasim.exchangeandroid.R
import kotlin.math.roundToInt


class RecyclerViewDivider(context: Context) : RecyclerView.ItemDecoration() {
    private val mDivider: Drawable?
    private val mContext: Context

    init {

        mDivider = ContextCompat.getDrawable(context, R.drawable.vertical_divider)
        mContext = context
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight


        val childCount = parent.adapter!!.itemCount

        for (i in 0 until childCount) {

            if (i == childCount - 1) {
                continue
            }

            val child = parent.getChildAt(i)

            if (child != null) {
                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = child.bottom + params.bottomMargin
                val bottom = top + mDivider!!.intrinsicHeight
                val dip = 52f
                val r = mContext.resources
                val px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.getDisplayMetrics()
                )
                mDivider.setBounds(px.roundToInt(), top, right, bottom)
                mDivider.draw(c)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
            outRect.setEmpty()
        } else
            outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
    }

}