package com.robert.behaviordemo.behavior

import android.content.Context
import android.graphics.Rect
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.robert.behaviordemo.R

class LotteryContentBehavior @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : CoordinatorLayout.Behavior<View>(context, attrs) {

    companion object {
        const val TAG = "LotteryContentBehavior"
    }

    private val mHeadRect = Rect()

    override fun onLayoutChild(parent: CoordinatorLayout?, child: View?, layoutDirection: Int): Boolean {
        if (parent != null && child != null) {
            Log.d(TAG, "parent ---> $parent, child --->$child")
            val dependencies = parent.getDependencies(child)

            val dependency = findDependency(dependencies)
            Log.d(TAG, "dependency ---> $dependency")

            if (dependency != null) {
                mHeadRect.set(dependency.left, dependency.bottom, dependency.right, dependency.bottom + child.measuredHeight)
                Log.d(TAG, "mHeadRect ---> $mHeadRect")
                child.layout(mHeadRect.left, mHeadRect.top, mHeadRect.right, mHeadRect.bottom)
            }
            return true
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        if (child != null && dependency != null) {
            Log.d(TAG, "parent ---> $parent,\n child --->$child,\n dependency --->$dependency")
            child.translationY = dependency.translationY
            return true
        }
        return super.onDependentViewChanged(parent, child, dependency)
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

    private fun isDependOn(dependency: View?) = dependency?.id == R.id.fl_anim_container


}