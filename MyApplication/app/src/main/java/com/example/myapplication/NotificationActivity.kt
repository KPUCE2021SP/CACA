package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.android.synthetic.main.activity_notification.*
//import com.example.myapplication.MyFirebaseMessagingService
import com.google.common.net.MediaType
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.squareup.okhttp.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

//import com.google.firebase.quickstart.fcm.R
//import com.google.firebase.quickstart.fcm.databinding.ActivityMainBinding

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        setContentView(R.layout.activity_notification)

        fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if(response.isSuccessful) {
                    Log.d(TAG, "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e(TAG, response.errorBody().toString())
                }
            } catch(e: Exception) {
                Log.e(TAG, e.toString())
            }
        }


        sendMessage.setOnClickListener(){
            val key = "cGfQhS5PRtSxcVoV1CJbH5:APA91bF8RO_4wqLW9eZD_AQH5ISJsI4RAnh7u2ni6qaYNYA2j1x5hEY7W9x85tZULdNu-pgtSeRwi3ijgjbW_LpUaDF_JRRSKrN9NhlYKWJ2PCFhEkFSZ2ezyHcKFz_fWUIV9IEalyy7"


            val PushNotification = PushNotification(
                NotificationData("StreetCat", "내가 쓴 게시글에 댓글이 달렸어요!"),
                key
            )
            sendNotification(PushNotification)
        }


    }
}