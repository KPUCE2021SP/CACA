package com.example.myapplication.Home_Board

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.myapplication.Location.AlarmReceiver
import com.example.myapplication.Notification.NotificationData
import com.example.myapplication.Notification.PushNotification
import com.example.myapplication.Notification.RetrofitInstance
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home_message.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeMessageActivity : AppCompatActivity() {
    val db: FirebaseFirestore = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_message)
        var fbAuth = FirebaseAuth.getInstance()
        var uid = fbAuth?.uid.toString()
        val FamilyName = intent.getStringExtra("FamilyName")


        var mutableListMessage: MutableList<String> = mutableListOf("a")
        var mutableListMessageDEVICE: MutableList<String> = mutableListOf("a")
        mutableListMessage.clear()
        mutableListMessageDEVICE.clear()

        db.collection("Chats").document(FamilyName.toString()).collection("FamilyMember")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("FamilyMember", "${document.id}")
//                            mutableListMessageUID.add(document.id)
                    // DEVICE
                    val docRef3 = db.collection("Member").document(document.id).collection("DEVICE").document("TOKEN")
                    docRef3.get()
                        .addOnSuccessListener { docName ->
                            if (docName != null) {
                                mutableListMessageDEVICE.add(docName.data?.get("deviceInfo").toString())
                                Log.d("device", mutableListMessageDEVICE.toString())
                            } else {
                                Log.d(ContentValues.TAG, "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }


//                    //Member 이름 가져와서 mutableNameList에 저장
                    val docRef2 = db.collection("Member").document(document.id)
                    docRef2.get()
                        .addOnSuccessListener { docName ->
                            if (docName != null) {
                                Log.d(
                                    ContentValues.TAG,
                                    "DocumentSnapshot data: ${docName.data}"
                                )
                                mutableListMessage.add(docName.data?.get("name").toString())

                                var adapter: ArrayAdapter<String> = ArrayAdapter(
                                    this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    mutableListMessage
                                )
                                spinner_message.adapter = adapter

                            } else {
                                Log.d(ContentValues.TAG, "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }
                }

            }


        var spinnerUID: String = ""
        spinner_message.setSelection(0, false)
        spinner_message.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerUID = mutableListMessageDEVICE[position].toString()
                etToken.setText(mutableListMessageDEVICE[position])
                Log.d("spinnerUID", spinnerUID)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        btnSend.setOnClickListener {
            val title = etTitle.text.toString()
            val message = etMessage.text.toString()
            val recipientToken = etToken.text.toString()
            if(title != "" && message != "" && recipientToken != "") {
                PushNotification(
                    NotificationData(title, message),
                    recipientToken
                )
                sendNotification(PushNotification(NotificationData(title, message),recipientToken))

            }
        }

    }
    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(AlarmReceiver.TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(AlarmReceiver.TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(AlarmReceiver.TAG, e.toString())
        }
    }
}