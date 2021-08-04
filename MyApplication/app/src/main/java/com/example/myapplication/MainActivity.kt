package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
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

    var fbAuth = FirebaseAuth.getInstance() // 로그인
    var fbFire = FirebaseFirestore.getInstance()

    var uid = fbAuth?.uid.toString() // uid
    var uemail = fbAuth?.currentUser?.email.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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

}