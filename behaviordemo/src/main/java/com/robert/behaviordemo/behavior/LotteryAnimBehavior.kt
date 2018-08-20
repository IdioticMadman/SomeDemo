package com.robert.behaviordemo.behavior

import android.content.Context
import android.graphics.Rect
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.robert.behaviordemo.R
import com.robert.behaviordemo.widget.NestLinearLayout


class LotteryAnimBehavior @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : CoordinatorLayout.Behavior<View>(context, attrs) {

    companion object {
        const val TAG = "LotteryAnimBehavior"
    }

    private val mHeadRect = Rect()

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        Log.d(TAG, "onStartNestedScroll : child ---> $child,\n directTargetChild ---> $directTargetChild,\n target ---> $target,\n")
        return axes.and(ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.d(TAG, "onNestedPreScroll : child ---> $child,\n target ---> $target,\n")
        if (target is RecyclerView) {
            val down = target.canScrollVertically(1)//是否可以向上推
            val up = target.canScrollVertically(-1)//是否可以向下拉
            Log.d(TAG, "方向 ---> down: $down,up :$up, dy:$dy")
            val maxTranslationY = getMaxTranslationY(child)
            Log.d(TAG, "当前偏移--->" + child.translationY + ", MAX ---> " + maxTranslationY)
            if (down && !up && dy < 0) {
                //向下滑
                val translationY = child.translationY
                if (translationY - dy <= 0) {
                    child.translationY = translationY - dy
                } else {
                    child.translationY = 0f
                }
            }
        }

        if (target is NestLinearLayout) {
            Log.d(TAG, "方向 ---> dy:$dy")
            val maxTranslationY = getMaxTranslationY(child)
            Log.d(TAG, "当前偏移--->" + child.translationY + ", MAX ---> " + maxTranslationY)
            if (dy < 0) {
                //向下滑
                val translationY = child.translationY
                if (translationY - dy <= 0) {
                    child.translationY = translationY - dy
                } else {
                    child.translationY = 0f
                }
            }
        }
    }

    private fun getMaxTranslationY(child: View): Int {
        return child.measuredHeight
    }

    override fun onLayoutChild(parent: CoordinatorLayout?, child: View?, layoutDirection: Int): Boolean {
        if (parent != null && child != null) {
            val dependencies = parent.getDependencies(child)
            val dependency = findDependency(dependencies)
            if (dependency != null) {
                mHeadRect.set(dependency.left, dependency.bottom, dependency.right, dependency.bottom + child.measuredHeight)
                child.layout(mHeadRect.left, mHeadRect.top, mHeadRect.right, mHeadRect.bottom)
                child.translationY = -child.measuredHeight.toFloat()
            }
            return true
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        return isDependOn(dependency)
    }

    private fun findDependency(views: List<View>): View? {
        views.forEach {
            if (isDependOn(it)) {
                return it
            }
        }
        return null
    }

    private fun isDependOn(dependency: View?) = dependency?.id == R.id.tab


}