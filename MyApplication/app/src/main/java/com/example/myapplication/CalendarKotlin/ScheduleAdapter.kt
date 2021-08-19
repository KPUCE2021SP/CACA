package com.example.myapplication.CalendarKotlin

import android.content.Context
import android.graphics.Rect
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.util.*
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

class ScheduleAdapter(val todoList : ArrayList<Schedule>) :
RecyclerView.Adapter<ScheduleAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoDay = itemView?.findViewById<TextView>(R.id.todolistTime)
        val todoTitle = itemView?.findViewById<TextView>(R.id.todolistTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("tag1" , "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(android.R.layout.simple_list_item_1,
//            parent, false)
        val view = inflater.inflate(R.layout.activity_schedulelist,
                parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.todoDay?.text = todoList[position].time
        holder.todoTitle?.text = todoList[position].title
        Log.d("tag1" , todoList[position].time + todoList[position].title)

    }

    override fun getItemCount() = todoList.size


}

//
//class VerticalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {
//
//    @Override
//    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
//        super.getItemOffsets(outRect, view, parent, state)
//        outRect.top = divHeight
//        outRect.bottom = divHeight
//    }
//}
//
//class HorizontalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {
//
//    @Override
//    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
//        super.getItemOffsets(outRect, view, parent, state)
//        outRect.left = divHeight
//        outRect.right = divHeight
//    }
//}



