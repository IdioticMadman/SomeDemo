package com.robert.behaviordemo

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout

class MainActivity : AppCompatActivity() {

    companion object {
        val tabTitle = arrayListOf("title1", "title2", "title3", "title4", "title5")
    }

    private val mFlCover: FrameLayout by bindView(R.id.fl_cover)

    private val mTabLayout: TabLayout by bindView(R.id.tab)
//    private val mRvHistory by bindView<RecyclerView>(R.id.rv_history)
//    private val mRvPlay by bindView<RecyclerView>(R.id.rv_play)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFlCover.setOnClickListener { mFlCover.visibility = View.GONE }
        tabTitle.forEach {
            val newTab = mTabLayout.newTab()
            newTab.text = it
            mTabLayout.addTab(newTab)
        }
//        mRvHistory.adapter = MyRvAdapter(this)
//        mRvPlay.adapter = MyRvAdapter(this)
//        mRvPlay.isNestedScrollingEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == R.id.cover) {
                mFlCover.visibility = View.VISIBLE
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
