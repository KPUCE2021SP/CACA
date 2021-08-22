package com.example.myapplication.CalendarKotlin

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_schedule_content.*
import kotlin.collections.ArrayList

//class ScheduleAdapter(data: OrderedRealmCollection<Schedule>):
//    RealmRecyclerViewAdapter<Schedule, ScheduleAdapter.ViewHolder>(data, true){
//
//    private var listener:((Long?)->Unit)? = null
//
//    fun setOnItenClickListener(listener:(Long?)->Unit){
//        this.listener = listener
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(android.R.layout.simple_list_item_1,
//            parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val schedule:Schedule? = getItem(position)
//        val startDate = schedule?.startTime?.let { Date(it) }
//        val timeText = DateFormat.format("HH:mm", startDate).toString()
//        holder.eventText.text = timeText+ " " + schedule?.title
//        holder.itemView.setOnClickListener {
//            listener?.invoke(schedule?.id)
//        }
//    }
//
//    class ViewHolder(cell:View):RecyclerView.ViewHolder(cell){
//        val eventText:TextView = cell.findViewById(android.R.id.text1)
//    }
//
//    init {
//        setHasStableIds(true)
//    }
//
//    override fun getItemId(position: Int): Long {
//        return getItem(position)?.id ?: 0
//    }
//}

class ScheduleAdapter(val scheduleList: ArrayList<Schedule>) :
RecyclerView.Adapter<ScheduleAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoDay = itemView?.findViewById<TextView>(R.id.todolistTime)
        val todoTitle = itemView?.findViewById<TextView>(R.id.todolistTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("tag1" , "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_schedulelist,
                parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.todoDay?.text = scheduleList[position].sche_title
        holder.todoTitle?.text = scheduleList[position].shce_content
        Log.d("tag1" , scheduleList[position].sche_title + scheduleList[position].shce_content)

        holder.itemView.setOnClickListener {


        }

    }

    override fun getItemCount() = scheduleList.size

    fun setItemClickListener(onItemClickListener: AdapterView.OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : AdapterView.OnItemClickListener




}




