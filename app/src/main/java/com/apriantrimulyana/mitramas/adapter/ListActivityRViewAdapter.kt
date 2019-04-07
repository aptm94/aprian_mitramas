package com.apriantrimulyana.mitramas.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.apriantrimulyana.mitramas.R
import com.apriantrimulyana.mitramas.model.ListModel
import kotlinx.android.synthetic.main.list_activity_rv_item.view.*
import java.util.*

class ListActivityRViewAdapter(private val activity: Activity, ListItems: MutableList<ListModel>) : RecyclerView.Adapter<ListActivityRViewAdapter.ViewHolder>() {
    private val inflater: LayoutInflater? = null
    private var ListItems: MutableList<ListModel>? = null
    private val arraylist: ArrayList<ListModel>

    init {
        this.ListItems = ListItems
        this.arraylist = ArrayList<ListModel>()
        this.arraylist.addAll(ListItems)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val j = ListItems!![position]

        holder.tvActivity.setText(j.getData2())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_activity_rv_item, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvActivity: TextView

        init {
            tvActivity = v.tv_activity
        }
    }

    override fun getItemCount(): Int {
        return ListItems!!.size
    }
}
