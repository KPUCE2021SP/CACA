package com.example.myapplication.FamilySet

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class StartActivity : AppCompatActivity() { // 잠깐 뜨는 화면
    private val SPLASH_TIME_OUT : Long=5000 //3s

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        startLoading()

    }
    private fun startLoading() {
        val handler= Handler()
        handler.postDelayed({finish()}, 1000)
    }
}