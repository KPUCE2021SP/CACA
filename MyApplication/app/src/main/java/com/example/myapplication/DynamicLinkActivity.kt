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


        // intent 넘겨서 들어왔을 경우 랜덤 코드 생성
        var characterTable = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "1", "2", "3", "4", "5", "6", "7", "8", "9") // 랜덤 코드 배열

        var code : String = ""
        for (i in 0..8) { // 랜덤 코드 8자리 생성
            val random = Random()
            val num = random.nextInt(characterTable.size)
            code += characterTable[num]
        }

        inviteCodeView.setText(code) // 8자리 코드 랜덤 생성해서 set



        // 링크 타고 들어왔을 경우
        if (intent.action == Intent.ACTION_VIEW) { // 링크 타고 들어왔을 때 수행하는 코드
            val value1 = intent.data?.getQueryParameter("key") // link의 key 값을 value로 받는다.
            inviteCodeView.setText(value1)
        }





        inviteCodeImage.setOnClickListener(){ // 이미지 누르면 8자리 코드 클립보드에 복사
            onClick_clipboard(inviteCodeView.text.toString())
        }

        var link = "https://familyship.page.link/DynamicLinkActivity?key=" + inviteCodeView.text.toString()


        shareBtn.setOnClickListener(){ // sharebutton 누르면 링크 자체 복사
            onClick_clipboard(link)
        }


    }

    fun onClick_clipboard(texttext : String){ // 클립 보드에 복사

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("라벨", texttext)
        clipboardManager!!.setPrimaryClip(clipData)

        Toast.makeText(applicationContext, "$texttext 복사완료", Toast.LENGTH_LONG).show()
    }

}