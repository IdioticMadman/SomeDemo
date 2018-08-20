package com.robert.behaviordemo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.robert.behaviordemo.R


class MyRvAdapter(private val context: Context) : RecyclerView.Adapter<MyRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = "第" + position + "条数据"
    }

    override fun getItemCount(): Int {
        return 40
    }

    class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

        val tv: TextView = itemView.findViewById(R.id.tv)
    }

}