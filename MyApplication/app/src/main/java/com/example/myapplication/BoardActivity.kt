package com.example.myapplication

import android.app.TabActivity
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_familysetting.*
import kotlinx.android.synthetic.main.activity_mainpage.*
import kotlinx.android.synthetic.main.board.*

//edit_board_content

class BoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board)


        val FamilyName = intent.getStringExtra("FamilyName") // 제목 선정
        //FamilyNameTextView.text = FamilyName


        var fbAuth = FirebaseAuth.getInstance() // 로그인
        var fbFire = FirebaseFirestore.getInstance()
        var uid = fbAuth?.uid.toString() // uid
        val db: FirebaseFirestore = Firebase.firestore


        val board_content = hashMapOf( // Family name
            "name" to uid
        )


        boardUpload.setOnClickListener(){
            var message = edit_board_content.text.toString()
            db.collection("Chats").document(FamilyName.toString()).collection("BOARD").document(message).set(board_content) // 게시판 활성화
            Toast.makeText(this, "게시판 업로드 완료!!", Toast.LENGTH_SHORT).show()
        }


    }
}