package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_layout.*
import kotlinx.android.synthetic.main.mypage_activity.*
import java.util.*


class MainActivity : AppCompatActivity() {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var viewAdapter: RecyclerView.Adapter<*>
//    private lateinit var viewManager: RecyclerView.LayoutManager

    private var isFabOpen = false

    var fbAuth = FirebaseAuth.getInstance() // 로그인
    var fbFire = FirebaseFirestore.getInstance()

    var uid = fbAuth?.uid.toString() // uid
    var uemail = fbAuth?.currentUser?.email.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // click animation
        fabMain.setOnClickListener {
            toggleFab()
        }

        // click event
        fabCreate.setOnClickListener {
            Toast.makeText(this,"가족생성 버튼 클릭", Toast.LENGTH_SHORT).show()
//            val intent= Intent(this, DynamicLinkActivity::class.java)
//            startActivity(intent)
        }

        fabInsert.setOnClickListener {
            Toast.makeText(this,"초대코드 입력 버튼 클릭", Toast.LENGTH_SHORT).show()
//            val intent= Intent(this, InsertInviteCode::class.java)
//            startActivity(intent)
        }


        val db: FirebaseFirestore = Firebase.firestore // 여러 document 받아오기
        var a = ""
        db.collection("Member").document(uid).collection("Familys")
            //.whereEqualTo("Familys", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    a += document.id + ","
                    val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater // 동적으로 생성

                    val containView = layoutInflater.inflate(R.layout.card_layout, null) // mypage_content를 inflate // card_layout을 inflate
                    val mVContentView = containView as View
                    val FamilyNameText = mVContentView.findViewById(R.id.item_title) as TextView
                    FamilyNameText.text = document.id

                    l_contain.addView(containView)


                }
                textView.text = a // Test용 받아오기

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


    }

    private fun toggleFab() {
//        Toast.makeText(this, "메인 플로팅 버튼 클릭 : $isFabOpen", Toast.LENGTH_SHORT).show()

        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션 세팅
        if (isFabOpen) {
            ObjectAnimator.ofFloat(fabCreate, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(fabInsert, "translationY", 0f).apply { start() }
            fabMain.setImageResource(R.drawable.ic_baseline_add_24)

            // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션 세팅
        } else {
            ObjectAnimator.ofFloat(fabCreate, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(fabInsert, "translationY", -400f).apply { start() }
            fabMain.setImageResource(R.drawable.ic_baseline_clear_24)
        }

        isFabOpen = !isFabOpen

    }

}