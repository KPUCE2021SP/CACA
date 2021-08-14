package com.example.myapplication.CalendarKotlin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
//import com.github.sundeepk.compactcalendarview.CompactCalendarView
//import com.github.sundeepk.compactcalendarview.domain.Event
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_schedule_content.*
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import kotlinx.android.synthetic.main.activity_schedule_main.*
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var realm:Realm

    private lateinit var selectedCalendar:Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_main)

        val thisDate = Date()
        thisDate.hours = 0
        thisDate.minutes = 0
        thisDate.seconds = 0
        selectedCalendar = Calendar.getInstance()
        selectedCalendar.time = thisDate
        val selectedDate = selectedCalendar.timeInMillis

        selectedDateLabel.text = DateFormat.format("yyyy/MM/dd", selectedDate)
//        toolbar.title = DateFormat.format("yyyy/MM", selectedDate)

        val realmConfig = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        realm = Realm.getInstance(realmConfig)

        list.layoutManager = LinearLayoutManager(this)

        compactcalendar_view.setFirstDayOfWeek(1)
        compactcalendar_view.removeAllEvents()
        var schedules = realm.where<Schedule>().findAll()
        for(schedule in schedules){
            val event = Event(Color.GREEN, schedule.startTime)
            compactcalendar_view.addEvent(event)
        }

        schedules = realm.where<Schedule>()
            .greaterThanOrEqualTo("startTime", selectedDate)
            .lessThan("startTime", selectedDate + 24*60*60*1000)
            .findAll()
            .sort("startTime")
        var adapter = ScheduleAdapter(schedules)
        list.adapter = adapter


        compactcalendar_view.setListener(
            object : CompactCalendarView.CompactCalendarViewListener {
                override fun onDayClick(dateClicked: Date) {
                    selectedCalendar.time = dateClicked
                    val selectedTimeInMills = selectedCalendar.timeInMillis
                    val dateFormat = DateFormat.format("yyyy/MM/dd", selectedTimeInMills)
                    selectedDateLabel.text = dateFormat

                    schedules = realm.where<Schedule>()
                        .greaterThanOrEqualTo("startTime", selectedTimeInMills)
                        .lessThan("startTime", selectedTimeInMills + 24*60*60*1000)
                        .findAll()
                        .sort("startTime")
                    adapter = ScheduleAdapter(schedules)
                    list.adapter = adapter
                    adapter.setOnItenClickListener { id->
                        val intent = Intent(this@CalendarActivity, ScheduleEditActivity::class.java)
                            .putExtra("schedule_id", id)
                        startActivity(intent)
                    }
                }
                override fun onMonthScroll(firstDayOfNewMonth: Date?) {
//                    toolbar.title = DateFormat.format("yyyy/MM", firstDayOfNewMonth)
                }
            })


        fab.setOnClickListener { view ->
            val intent = Intent(this, ScheduleEditActivity::class.java)
                .putExtra("selected_date", selectedDateLabel.text.toString())
            startActivity(intent)
        }
        adapter.setOnItenClickListener { id->
            val intent = Intent(this, ScheduleEditActivity::class.java)
                .putExtra("schedule_id", id)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}

