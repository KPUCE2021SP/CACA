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

        var key = "a"

        if (intent.action == Intent.ACTION_VIEW) { // 앱 링크는 해당 인텐트 필터로만 가능하기 때문에
            val value1 = intent.data?.getQueryParameter("key")
            dynamicTextView.setText(value1)
        }


    }

}