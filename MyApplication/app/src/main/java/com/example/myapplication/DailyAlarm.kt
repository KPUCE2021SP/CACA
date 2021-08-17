package com.example.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.myapplication.FamilySet.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyAlarm : BroadcastReceiver() {


    var fbAuth = FirebaseAuth.getInstance() // 로그인
    var fbFire = FirebaseFirestore.getInstance()

    var uid = fbAuth?.uid.toString() // uid
    var uemail = fbAuth?.currentUser?.email.toString()

    val db: FirebaseFirestore = Firebase.firestore

    // location
    var fname : String  = ""
    var str : String = ""

    var lat : Double = 1.0
    var log : Double = 1.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationManager : LocationManager? = null

    private lateinit var locationCallback: LocationCallback
    // location

    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Received intent : $intent")
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        deliverNotification(context)
    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("mm") // 정각마다 알람
        val formatted = current.format(formatter)

        if(formatted == "00") {
            val builder = /////////////////////////////////////////////////////////////alarm
                NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                    .setSmallIcon(R.drawable.familyship)
                    .setContentTitle("앗! 정각입니다요!!")
                    .setContentText("알람이 있나 확인해 볼깝쇼?")
                    .setContentIntent(contentPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)

            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }



    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Stand up notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "AlarmManager Tests"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
