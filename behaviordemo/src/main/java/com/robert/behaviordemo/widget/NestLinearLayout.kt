package com.robert.behaviordemo.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.NestedScrollingChild2
import android.support.v4.view.NestedScrollingChildHelper
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MASK
import android.widget.LinearLayout

class NestLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        LinearLayout(context, attrs, defStyle), NestedScrollingChild2 {

    companion object {
        const val TAG = "NestLinearLayout"
    }

    private val mScrollingChildHelper = NestedScrollingChildHelper(this)

    private var mLastTouchX: Int = 0
    private var mLastTouchY: Int = 0

    private val mScrollOffset = IntArray(2)
    private val mScrollConsumed = IntArray(2)
    private val mNestedOffsets = IntArray(2)

    init {
        isNestedScrollingEnabled = true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.e(TAG, "onInterceptTouchEvent ---> ${actionToString(ev?.action ?: -1)}")
        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e(TAG, "dispatchTouchEvent ---> ${actionToString(ev?.action ?: -1)}")
        return super.dispatchTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e(TAG, "onTouchEvent ---> ${actionToString(event?.action ?: -1)}")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastTouchX = (event.x + 0.5f).toInt()
                mLastTouchY = (event.y + 0.5f).toInt()
                val nestedScrollAxis = ViewCompat.SCROLL_AXIS_NONE or ViewCompat.SCROLL_AXIS_VERTICAL
                startNestedScroll(nestedScrollAxis, ViewCompat.TYPE_TOUCH)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val x = (event.x + 0.5f).toInt()
                val y = (event.y + 0.5f).toInt()
                val dx = mLastTouchX - x
                val dy = mLastTouchY - y
                Log.e(TAG, "onTouchEvent ---> dx:$dx ,dy:$dy")
                dispatchNestedPreScroll(dx, dy, mScrollConsumed, mScrollOffset, ViewCompat.TYPE_TOUCH)
                mLastTouchX = x
                mLastTouchY = y
            }
            MotionEvent.ACTION_UP -> {
                stopNestedScroll(ViewCompat.TYPE_TOUCH)
            }
        }
        return super.onTouchEvent(event)
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mScrollingChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?,
                                         offsetInWindow: IntArray?, type: Int): Boolean {
        return mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun stopNestedScroll(type: Int) {
        return mScrollingChildHelper.stopNestedScroll(type)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return mScrollingChildHelper.hasNestedScrollingParent(type)
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
                                      offsetInWindow: IntArray?, type: Int): Boolean {
        return mScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed, offsetInWindow, type)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mScrollingChildHelper.startNestedScroll(axes, type)
    }

    fun actionToString(action: Int): String {
        when (action) {
            MotionEvent.ACTION_DOWN -> return "ACTION_DOWN"
            MotionEvent.ACTION_UP -> return "ACTION_UP"
            MotionEvent.ACTION_CANCEL -> return "ACTION_CANCEL"
            MotionEvent.ACTION_OUTSIDE -> return "ACTION_OUTSIDE"
            MotionEvent.ACTION_MOVE -> return "ACTION_MOVE"
            MotionEvent.ACTION_HOVER_MOVE -> return "ACTION_HOVER_MOVE"
            MotionEvent.ACTION_SCROLL -> return "ACTION_SCROLL"
            MotionEvent.ACTION_HOVER_ENTER -> return "ACTION_HOVER_ENTER"
            MotionEvent.ACTION_HOVER_EXIT -> return "ACTION_HOVER_EXIT"
            MotionEvent.ACTION_BUTTON_PRESS -> return "ACTION_BUTTON_PRESS"
            MotionEvent.ACTION_BUTTON_RELEASE -> return "ACTION_BUTTON_RELEASE"
        }
        val index = action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
        when (action and ACTION_MASK) {
            MotionEvent.ACTION_POINTER_DOWN -> return "ACTION_POINTER_DOWN($index)"
            MotionEvent.ACTION_POINTER_UP -> return "ACTION_POINTER_UP($index)"
            else -> return Integer.toString(action)
        }
    }
}