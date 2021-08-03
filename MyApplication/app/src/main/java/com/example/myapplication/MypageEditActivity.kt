package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.mypage_edit.*
import java.util.ArrayList
import java.util.regex.Pattern

class MypageEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage_edit)



        list_which.setOnClickListener {
            var listArray = arrayOf("약 이름", "진단결과", "수술병원 & 의사", "생일", "전화번호", "주소")
            var dlg = AlertDialog.Builder(this)
            dlg.setTitle("목록을 선택해주세요")
            dlg.setIcon(R.drawable.familyship)
            dlg.setItems(listArray) { dialog, which -> list_which.setText(listArray[which])
                editImage(list_which.text.toString())}
            dlg.setPositiveButton("닫기", null)
            dlg.show()


        }
    }
    fun editImage(itemName: String){
        if (itemName == "약 이름"){
            list_imageView.setImageResource(R.drawable.ic_input_medicine)
        } else if(itemName == "진단결과"){
            list_imageView.setImageResource(R.drawable.ic_input_hospital)
        } else if(itemName == "수술병원 & 의사"){
            list_imageView.setImageResource(R.drawable.ic_input_doctor)
        } else if(itemName == "생일"){
            list_imageView.setImageResource(R.drawable.cake)
        } else if(itemName == "전화번호"){
            list_imageView.setImageResource(R.drawable.calling)
        } else if(itemName == "주소"){
            list_imageView.setImageResource(R.drawable.home)
        } else {

        }
    }

}