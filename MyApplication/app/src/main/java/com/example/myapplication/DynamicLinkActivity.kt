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
import java.util.*
import java.util.stream.IntStream.range

class DynamicLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)



        dynamicTextView.setText("asdfasdfasdfasdfasdfasdf") // 기본 textView

        if (intent.action == Intent.ACTION_VIEW) { // 링크 타고 들어왔을 때 수행하는 코드
            val value1 = intent.data?.getQueryParameter("key") // link의 key 값을 value로 받는다.
            dynamicTextView.setText(value1) // 확인용
        }



        dynamicTextView.setOnClickListener(){ // textView 클릭하면 text 클립보드에 복사됨
            onClick_clipboard()
        }



        var characterTable = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "1", "2", "3", "4", "5", "6", "7", "8", "9") // 랜덤 코드 배열

        var code : String = ""
        for (i in 0..8) { // 랜덤 코드 8자리 생성
            val random = Random()
            val num = random.nextInt(characterTable.size)
            code += characterTable[num]
        }

        randomTextView.setText(code)


    }

    fun onClick_clipboard(){ // 클립 보드에 복사

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("라벨", dynamicTextView.text.toString())
        clipboardManager!!.setPrimaryClip(clipData)

        Toast.makeText(applicationContext, dynamicTextView.text.toString() + " 복사완료", Toast.LENGTH_LONG).show()
    }

}