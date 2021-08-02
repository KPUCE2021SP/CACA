package com.example.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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

        dynamicTextView.setText("asdfasdfasdfasdfasdfasdf")
//
//        dynamicTextView.setOnClickListener(){
//            copyStr(Context, dynamicTextView.text.toString())
//
//        }


    }

//    fun copyStr(context: Context, str : String){
//        val clipboardManager : ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//        val clipData = ClipData.newPlainText("STRNAME", str)
//        clipboardManager.primaryClip = clipData
//
//        Toast.makeText(context, "복사되었습니다.", Toast.LENGTH_LONG).show()
//    }

}