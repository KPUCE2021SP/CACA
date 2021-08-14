package com.example.myapplication.CalendarKotlin

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.sip.SipSession
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment:DialogFragment(), DatePickerDialog.OnDateSetListener{

    interface OnDateSelectedListener{
        fun onSelected(year:Int, month:Int, date:Int)
    }

    private var listener : OnDateSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when(context){
            is OnDateSelectedListener -> listener = context
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireActivity(), this, year, month, date)
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener?.onSelected(year, month, dayOfMonth)
    }
}

class TimePickerFragment:DialogFragment()
    , TimePickerDialog.OnTimeSetListener{
    interface OnTimeSelectedListener{
        fun onSelected(hourOfDay:Int, minute:Int)
    }
    private var listener:OnTimeSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when(context){
            is OnTimeSelectedListener-> listener   = context
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hourOfDay = c.get(Calendar.HOUR_OF_DAY)
        val minute  = c.get(Calendar.MINUTE)
        return TimePickerDialog(context, this, hourOfDay, minute, true)
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener?.onSelected(hourOfDay, minute)
    }
}

class SaveConfirmFragment:DialogFragment(){
    interface SaveListener{
        fun onSave()
    }
    private var listener:SaveListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when(context){
            is SaveListener-> listener = context
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("スケジュールを保存しますか？")
        builder.setNegativeButton("保存する"){dialog, which->
            listener?.onSave()
        }
        builder.setPositiveButton("キャンセル"){dialog, which->
       }
        return builder.create()
    }
}

class DeleteConfirmFragment:DialogFragment(){
    interface DeleteListener {
        fun onDelete()
    }
    private var listener:DeleteListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when(context){
            is DeleteListener->listener = context
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("編集中のスケジュールを消去しますか？")
        builder.setPositiveButton("消去する"){dialog, which ->
            listener?.onDelete()
        }
        builder.setNegativeButton("編集を続ける"){dialog, which ->  }
        return builder.create()
    }
}

class InvalidTimeFragment:DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("予定の終了時間が開始時間の前になっています！")
        builder.setNeutralButton("編集"){dialog, which ->  }
        return builder.create()
    }
}
















