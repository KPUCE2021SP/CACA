package com.example.myapplication.CalendarKotlin

import android.app.Application
import io.realm.Realm

class MyCalendarApplication : Application() { // DB 이제 필요 없을듯 하다.
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}