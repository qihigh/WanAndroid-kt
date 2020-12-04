/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * 继承自SwipeRefreshLayout,重写measure和layout。
 *
 * 使其支持多个子view,但子view目前仅支持铺满整个布局
 *  (布局时没有获取子view的margin信息，所以不支持margin)
 */
class ESwpLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    fun setTargetRecyclerView(target: RecyclerView) {
        try {
            val field = SwipeRefreshLayout::class.java.getDeclaredField("mTarget")
            field.isAccessible = true
            field.set(this, target)
            field.isAccessible = false
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        // 转移监听（防止反射被系统限制的情况下，进行补充处理）
        setOnChildScrollUpCallback { _, _ ->
            return@setOnChildScrollUpCallback target.canScrollVertically(-1)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val count = childCount
        val width = measuredWidth
        val height = measuredHeight

        super.onLayout(changed, left, top, right, bottom)

        // 布局其他几个子view。emptyView、placeHolderView、errorView

        val childLeft = paddingLeft
        val childTop = paddingTop
        val childWidth = width - paddingLeft - paddingRight
        val childHeight = height - paddingTop - paddingBottom

        (0 until count).map { getChildAt(it) }.forEach { child ->
            if (child is FrameLayout && child.visibility != View.GONE) {
                // 找到额外的view，进行布局
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val count = childCount

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 布局其他几个子view。emptyView、placeHolderView、errorView
        val aWidthMeasureSpec = measuredWidth - paddingLeft - paddingRight
        val aHeightMeasureSpec = measuredHeight - paddingTop - paddingBottom
        (0 until count).map { getChildAt(it) }.forEach { child ->
            if (child is FrameLayout && child.visibility != View.GONE) {
                // 找到额外的view，进行布局
                child.measure(
                    MeasureSpec.makeMeasureSpec(aWidthMeasureSpec, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(aHeightMeasureSpec, MeasureSpec.EXACTLY)
                )
            }
        }
    }
}
