package com.example.myapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.mypage_activity.*

class MypageActivity : AppCompatActivity() {

    var fbAuth = FirebaseAuth.getInstance()
    var fbFire = FirebaseFirestore.getInstance()

    var uid = fbAuth?.uid.toString()
    var uemail = fbAuth?.currentUser?.email.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage_activity)

        textViewName.setText(uid)

        val db : FirebaseFirestore = Firebase.firestore

        val city = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA"
        )

        db.collection("Member").document("ID_MEMBER")
            .update("city", true)
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

    }

}