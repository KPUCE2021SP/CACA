package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dynamic.*

class DynamicLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)


        if (intent.action == Intent.ACTION_VIEW) { // 링크 타고 들어왔을 때 수행하는 코드
            val value1 = intent.data?.getQueryParameter("key") // link의 key 값을 value로 받는다.
            dynamicTextView.setText(value1) // 확인용
        }


    }

}