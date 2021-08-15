package com.example.myapplication.Home_Board

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import com.example.myapplication.R
import com.example.myapplication.SerachMap.db
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_board_content.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.notice_card.*

class BoardContentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_content)

        var formatted1 : String = "asdf"

        var intent = intent

        var FamilyName1 = intent.getStringExtra("FamilyNameB") // 제목 선정
        var notice_board1 = intent.getStringExtra("notice_boardB") // 제목 선정
        var notice_name1 = intent.getStringExtra("notice_nameB") // 제목 선정
        var notice_profile1 = intent.getStringExtra("notice_profileB") // 제목 선정
        var BoardImageName1 = intent.getStringExtra("notice_imageB")

        //// profile Image
        val storage = Firebase.storage
        val storageRef = storage.reference
        val profileRef1 = notice_profile1?.let { storage.getReferenceFromUrl(it) }
        profileRef1?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            notice_profileB.setImageBitmap(profilebmp) // 작성한 사람 uid로 profileImage 변경!
        }?.addOnFailureListener {
            Toast.makeText(
                this,
                "image downloade failed",
                Toast.LENGTH_SHORT
            ).show()
        }

        //// Board Image
        val storage2 = Firebase.storage
        val storageRef2 = storage.reference
        val profileRef2 = BoardImageName1?.let { storage.getReferenceFromUrl(it) }
        profileRef2?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val profilebmp2 = BitmapFactory.decodeByteArray(it, 0, it.size)
            notice_imageB.setImageBitmap(profilebmp2) // 작성한 사람 uid로 profileImage 변경!
        }?.addOnFailureListener {
            Toast.makeText(
                this,
                "image downloade failed",
                Toast.LENGTH_SHORT
            ).show()
            notice_imageB.isGone =
                true                            // 업로드된 이미지가 없다면
        }

        formatted1 = intent.getStringExtra("formattedB").toString()
        if (formatted1 != null) {
            Log.d("Fmt",formatted1)
        }
        aaaaaaa1.text = FamilyName1

        notice_boardB.text = notice_board1
        notice_nameB.text = notice_name1
        notice_timeB.text = formatted1

        notice_imageB.background =
            getResources().getDrawable(R.drawable.imageview_cornerround, null)
        notice_imageB.setClipToOutline(true)

    }
}