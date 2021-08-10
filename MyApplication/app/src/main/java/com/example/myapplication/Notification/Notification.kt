package com.example.myapplication.Notification

import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.R
import com.google.android.gms.tasks.OnCompleteListener
//import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic2"

class Notification : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            FirebaseService.token = it.token
//            etToken.setText(it.token)
//        }



        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result


            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()


            //
            FirebaseService.token = msg
            etToken.setText(msg)
            //
        })
//        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)



        btnSend.setOnClickListener {
            val title = etTitle.text.toString()
            val message = etMessage.text.toString()
            val recipientToken = etToken.text.toString()
            if(title != "" && message != "" && recipientToken != "") {
                PushNotification(
                    NotificationData(title, message),
                    recipientToken
                )
//                    .also {
//                    sendNotification(it)
                    sendNotification(PushNotification(NotificationData(title, message),recipientToken))

//                }
            }
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
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
}