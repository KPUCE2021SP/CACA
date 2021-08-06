package com.example.myapplication

import android.app.TabActivity
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_mainpage.*

//edit_board_content

class BoardActivity : TabActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board)


        var fbAuth = FirebaseAuth.getInstance() // 로그인
        var fbFire = FirebaseFirestore.getInstance()
        var uid = fbAuth?.uid.toString() // uid
        val db: FirebaseFirestore = Firebase.firestore



        // DB에 값 입력
        db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val layoutInflater =
                        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val containView = layoutInflater.inflate(
                        R.layout.notice_card,
                        null
                    ) // mypage_content를 inflate
                    Board_LinearLayout.addView(containView)

                    val ContentView = containView as View
                    var notice_board = ContentView.findViewById(R.id.notice_board) as TextView

                    notice_board.text = document.id.toString()
                }
            }



    }
}