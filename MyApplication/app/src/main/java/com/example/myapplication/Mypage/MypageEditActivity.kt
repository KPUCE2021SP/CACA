package com.example.myapplication.Mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.mypage_edit.*

class MypageEditActivity : AppCompatActivity() {
    private lateinit var dataName :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage_edit)

        //팝업창 띄워서 목록 변경하기
        list_which.setOnClickListener {
            var listArray = arrayOf("약 이름", "진단결과", "수술병원 & 의사", "생일", "전화번호", "주소")
            var dlg = AlertDialog.Builder(this)
            dlg.setTitle("목록을 선택해주세요")
            dlg.setIcon(R.drawable.familyship)
            dlg.setItems(listArray) { dialog, which -> list_which.setText(listArray[which])
                editImage(list_which.text.toString())
                }
            dlg.setPositiveButton("닫기", null)
            dlg.show()
        }

        //저장하기
//        mypageSaveBtn.setOnClickListener {
//            makeDataName(list_which.text.toString())
//            updateData()
//        }

    }

    //DB에 업데이트하기
    private fun updateData(){
        var fbAuth = FirebaseAuth.getInstance()
        //firestore에 넣기
        val db : FirebaseFirestore = Firebase.firestore
        var uid = fbAuth?.uid.toString()
        var map= mutableMapOf<String,Any>()
        map[dataName] = mypage_edittext.text.toString()
        db.collection("Member").document(uid).update(map)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext, "업데이트 되었습니다", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MypageActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }

    //DB 저장을 위한 이름 바꿔주기
    fun makeDataName(itemName: String){
        if (itemName == "약 이름"){
            dataName = "medicine"
        } else if(itemName == "진단결과"){
            dataName ="diseaseName"
        } else if(itemName == "수술병원 & 의사"){
            dataName = "doctor"
        } else if(itemName == "생일"){
            dataName = "birthday"
        } else if(itemName == "전화번호"){
            dataName = "phoneNumber"
        } else if(itemName == "주소"){
            dataName = "address"
        } else {

        }
    }
    //이미지 변경
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