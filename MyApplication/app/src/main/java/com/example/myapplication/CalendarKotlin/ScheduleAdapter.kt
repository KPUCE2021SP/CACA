package com.example.myapplication.CalendarKotlin

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.util.*

class ScheduleAdapter(data:OrderedRealmCollection<Schedule>):
    RealmRecyclerViewAdapter<Schedule, ScheduleAdapter.ViewHolder>(data, true){

    private var listener:((Long?)->Unit)? = null

    fun setOnItenClickListener(listener:(Long?)->Unit){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_1,
            parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule:Schedule? = getItem(position)
        val startDate = schedule?.startTime?.let { Date(it) }
        val timeText = DateFormat.format("HH:mm", startDate).toString()
        holder.eventText.text = timeText+ " " + schedule?.title
        holder.itemView.setOnClickListener {
            listener?.invoke(schedule?.id)
        }
    }

    class ViewHolder(cell:View):RecyclerView.ViewHolder(cell){
        val eventText:TextView = cell.findViewById(android.R.id.text1)
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?: 0
    }
}