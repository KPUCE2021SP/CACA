package com.example.myapplication


import android.app.TabActivity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mainpage.*

class MainPageActivity : TabActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)

        val FamilyName = intent.getStringExtra("FamilyName") // 제목 선정
        FamilyNameTextView.text = FamilyName



        var fbAuth = FirebaseAuth.getInstance() // 로그인
        var fbFire = FirebaseFirestore.getInstance()
        var uid = fbAuth?.uid.toString() // uid
        val db: FirebaseFirestore = Firebase.firestore
        val docRef2 = db.collection("Member").document(uid).collection("MYPAGE").document(FamilyName.toString())
        docRef2.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    FamilyNameTextView.text = document.data?.get("name").toString() // family name 넣기

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }













        val tabHost = this.tabHost

        val tabSpecHome = tabHost.newTabSpec("HOME").setIndicator("메인")
        tabSpecHome.setContent(R.id.tabHome)
        tabHost.addTab(tabSpecHome)

        val tabSpecBoard = tabHost.newTabSpec("BOARD").setIndicator("게시판")
        tabSpecBoard.setContent(R.id.tabBoard)
        tabHost.addTab(tabSpecBoard)

        val tabSpecCalendar = tabHost.newTabSpec("CALENDAR").setIndicator("달력")
        tabSpecCalendar.setContent(R.id.tabCalendar)

        tabHost.addTab(tabSpecCalendar)

        val tabSpecAlbum = tabHost.newTabSpec("ALBUM").setIndicator("앨범")
        tabSpecAlbum.setContent(R.id.tabAlbum)
        tabHost.addTab(tabSpecAlbum)

        tabHost.currentTab = 0

        btnMain1.setOnClickListener {
            LinearMainpage.visibility = View.GONE
            LinearMain1.visibility = View.VISIBLE
            LinearMain2.visibility = View.GONE
            LinearMain3.visibility = View.GONE
            LinearMain4.visibility = View.GONE
            btnMain1.setTextColor(Color.WHITE)
        }
        btnMain2.setOnClickListener {
            LinearMainpage.visibility = View.GONE
            LinearMain1.visibility = View.GONE
            LinearMain2.visibility = View.VISIBLE
            LinearMain3.visibility = View.GONE
            LinearMain4.visibility = View.GONE
            btnMain1.setTextColor(Color.WHITE)
        }
        btnMain3.setOnClickListener {
            LinearMainpage.visibility = View.GONE
            LinearMain1.visibility = View.GONE
            LinearMain2.visibility = View.GONE
            LinearMain3.visibility = View.VISIBLE
            LinearMain4.visibility = View.GONE
            btnMain1.setTextColor(Color.WHITE)
        }
        btnMain4.setOnClickListener {
            LinearMainpage.visibility = View.GONE
            LinearMain1.visibility = View.GONE
            LinearMain2.visibility = View.GONE
            LinearMain3.visibility = View.GONE
            LinearMain4.visibility = View.VISIBLE
            btnMain1.setTextColor(Color.WHITE)
        }











        btnMyPage.setOnClickListener {
            val intent = Intent(application, MypageActivity::class.java)
            startActivity(intent)
        }

        btnGroup.setOnClickListener {
            val intent = Intent(application, DynamicLinkActivity::class.java)
            startActivity(intent)
        }
    }
}