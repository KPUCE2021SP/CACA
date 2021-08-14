package com.example.myapplication.CalendarKotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import com.example.myapplication.R
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ScheduleEditActivity : AppCompatActivity()
    , DatePickerFragment.OnDateSelectedListener
    , TimePickerFragment.OnTimeSelectedListener
    , SaveConfirmFragment.SaveListener
    , DeleteConfirmFragment.DeleteListener{

    private lateinit var realm:Realm
    private var editStartDateFlag = true
    private var editStartTimeFlag = true
    private var scheduleId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)


        val realmConfig = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        realm = Realm.getInstance(realmConfig)

        scheduleId = intent.getLongExtra("schedule_id", -1L)

        if(scheduleId != -1L){
            val schedule = realm.where<Schedule>()
                .equalTo("id", scheduleId).findFirst()
            titleEdit.setText(schedule?.title)
            placeEdit.setText(schedule?.place)
            startDateEdit.setText(DateFormat.format("yyyy/MM/dd",
                schedule?.startTime?.let { Date(it) }))
            startTimeEdit.setText(DateFormat.format("HH:mm",
                schedule?.startTime?.let { Date(it) }))
            endDateEdit.setText(DateFormat.format("yyyy/MM/dd",
                schedule?.endTime?.let { Date(it) }))
            endTimeEdit.setText(DateFormat.format("HH:mm",
                schedule?.endTime?.let { Date(it) }))
            detailEdit.setText(schedule?.detail)
            saveButton.text = "Update"
            deleteButton.visibility = View.VISIBLE
        }else{
            val selectedDate = intent.getStringExtra("selected_date")
            startDateEdit.setText(selectedDate)
            endDateEdit.setText(selectedDate)
            val c = Calendar.getInstance()
            startTimeEdit.setText("%1$02d:%2$02d"
                .format(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)))
            endTimeEdit.setText("%1$02d:%2$02d"
                .format(c.get(Calendar.HOUR_OF_DAY) + 1, c.get(Calendar.MINUTE)))
            deleteButton.text = "Cancel"
        }


        startDateEdit.setOnClickListener {
            editStartDateFlag = true
            val dialog = DatePickerFragment()
            dialog.show(supportFragmentManager, "startDate_dialog")
        }
        endDateEdit.setOnClickListener {
            editStartDateFlag = false
            val dialog = DatePickerFragment()
            dialog.show(supportFragmentManager, "endDate_dialog")
        }
        startTimeEdit.setOnClickListener {
            editStartTimeFlag = true
            val dialog = TimePickerFragment()
            dialog.show(supportFragmentManager, "startTime_dialog")
        }
        endTimeEdit.setOnClickListener {
            editStartTimeFlag = false
            val dialog = TimePickerFragment()
            dialog.show(supportFragmentManager, "endTime_dialog")
        }
        saveButton.setOnClickListener {view:View->
            val startDateTime = (startDateEdit.text.toString() + " "+ startTimeEdit.text.toString())
                .toDate()?.time
            val endDateTime = (endDateEdit.text.toString() + " " + endTimeEdit.text.toString())
                .toDate()?.time
            if (startDateTime != null && endDateTime != null) {
                if(startDateTime <= endDateTime){
                    val dialog = SaveConfirmFragment()
                    dialog.show(supportFragmentManager, "saveConfirm_dialog")
                }else{
                    val dialog = InvalidTimeFragment()
                    dialog.show(supportFragmentManager, "invalidTime_dialog")
                }
            }
        }
        deleteButton.setOnClickListener {view:View->
            val dialog = DeleteConfirmFragment()
            dialog.show(supportFragmentManager, "deleteConfirm_dialog")
        }

        mapButton.setOnClickListener { view:View->
            val place = placeEdit.text.toString()
            val uri = Uri.parse("geo:0,0?q=$place")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun String.toDate(pattern:String = "yyyy/MM/dd HH:mm"): Date?{
        return try{
            SimpleDateFormat(pattern).parse(this)
        }catch (e:IllegalArgumentException){
            return null
        }catch (e:ParseException){
            return null
        }
    }

    override fun onSelected(year: Int, month: Int, date: Int) {
        val c = Calendar.getInstance()
        c.set(year, month, date)
        if(editStartDateFlag){
            startDateEdit.setText(DateFormat.format("yyyy/MM/dd", c))
        }else{
            endDateEdit.setText(DateFormat.format("yyyy/MM/dd", c))
        }
    }
    override fun onSelected(hourOfDay: Int, minute: Int) {
        if(editStartTimeFlag){
            startTimeEdit.setText("%1$02d:%2$02d".format(hourOfDay, minute))
        }else{
            endTimeEdit.setText("%1$02d:%2$02d".format(hourOfDay, minute))
        }
    }

    override fun onSave() {
        when(scheduleId){
            -1L->{
                realm.executeTransaction { db:Realm->
                    val maxId = db.where<Schedule>().max("id")
                    val nextId = (maxId?.toLong() ?: 0L) + 1
                    val schedule = db.createObject<Schedule>(nextId)
                    val startTime =(startDateEdit.text.toString() + " " + startTimeEdit.text.toString())
                        .toDate("yyyy/MM/dd HH:mm")?.time
                    schedule.startTime = startTime ?: Date().time
                    val endTime =(endDateEdit.text.toString() + " " + endTimeEdit.text.toString())
                        .toDate("yyyy/MM/dd HH:mm")?.time
                    schedule.endTime = endTime ?: Date().time
                    schedule.title = titleEdit.text.toString()
                    schedule.place = placeEdit.text.toString()
                    schedule.detail = detailEdit.text.toString()
                }
            }
            else ->{
                realm.executeTransaction { db:Realm->
                    val schedule = db.where<Schedule>()
                        .equalTo("id", scheduleId).findFirst()
                    val startTime =(startDateEdit.text.toString() + " " + startTimeEdit.text.toString())
                        .toDate("yyyy/MM/dd HH:mm")?.time
                    schedule?.startTime = startTime ?: Date().time
                    val endTime =(endDateEdit.text.toString() + " " + endTimeEdit.text.toString())
                        .toDate("yyyy/MM/dd HH:mm")?.time
                    schedule?.endTime = endTime ?: Date().time
                    schedule?.title = titleEdit.text.toString()
                    schedule?.place = placeEdit.text.toString()
                    schedule?.detail = detailEdit.text.toString()
                }
            }
        }
        val intent = Intent(this, CalendarActivity::class.java)
        startActivity(intent)
    }

    override fun onDelete() {
        if(scheduleId != -1L){
            realm.executeTransaction {db:Realm->
                db.where<Schedule>().equalTo("id", scheduleId)
                    .findFirst()?.deleteFromRealm()
            }
        }
        val intent = Intent(this, CalendarActivity::class.java)
        startActivity(intent)
    }

        override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
