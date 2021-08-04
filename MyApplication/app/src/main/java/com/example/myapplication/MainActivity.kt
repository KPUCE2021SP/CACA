package com.example.myapplication

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.mypage_activity.*

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
                    a += document.id
                }
                textView.text = a

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }




//        viewManager = LinearLayoutManager(this, HORIZONTAL, true)
//        viewAdapter = MyAdapter()
//
//
//        recyclerView = findViewById<RecyclerView>(R.id.recyclerview_main).apply {
//            // use this setting to improve performance if you know that changes
//            // in content do not change the layout size of the RecyclerView
//            setHasFixedSize(true)
//
//            // use a linear layout manager
//            layoutManager = viewManager
//
//            // specify an viewAdapter (see also next example)
//            adapter = viewAdapter
//
//        }

        groupAddBtn.setOnClickListener(){
            val intent= Intent(this, DynamicLinkActivity::class.java)
            startActivity(intent)
        }

    }
}