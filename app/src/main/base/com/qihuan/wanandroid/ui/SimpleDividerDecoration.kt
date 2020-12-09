/* (C)2020 */
package com.qihuan.wanandroid.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.AdaptScreenUtils
import com.qihuan.wanandroid.R

/**
 * 分割线，自带颜色。
 */
class SimpleDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val dividerHeight: Int = AdaptScreenUtils.pt2Px(1F)
    private val dividerPaint: Paint = Paint()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom.toFloat()

            val bottom = view.bottom + dividerHeight.toFloat()
            c.drawRect(left.toFloat(), top, right.toFloat(), bottom, dividerPaint)
        }
    }

    init {
        dividerPaint.color = ContextCompat.getColor(context, R.color.w_separator)
    }
}
