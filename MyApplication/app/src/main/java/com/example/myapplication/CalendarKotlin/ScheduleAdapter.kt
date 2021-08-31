package com.example.myapplication.CalendarKotlin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
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
            itemClickListner.onClick(it, position)
        }
    }


    override fun getItemCount() = scheduleList.size



    //클릭 인터페이스 정의
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener

    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

}




