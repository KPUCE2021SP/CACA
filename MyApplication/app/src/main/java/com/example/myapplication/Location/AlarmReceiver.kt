package com.example.myapplication.Location
///////////////////////////////////////////////// 쓰는거 지우지마세요 (알람)
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
import com.example.myapplication.DailyAlarm
import com.example.myapplication.FamilySet.MainActivity
import com.example.myapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_location.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AlarmReceiver : BroadcastReceiver() {


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

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("mm") // 정각마다 알람
//        val formatted = current.format(formatter)
//
//        Log.d("timetime", formatted)
//
//
//
//        if(formatted != "") { // 복구 예정
//            val builder = /////////////////////////////////////////////////////////////alarm
//                    NotificationCompat.Builder(context, DailyAlarm.PRIMARY_CHANNEL_ID)
//                            .setSmallIcon(R.drawable.familyship)
//                            .setContentTitle("앗! 정각입니다요!!")
//                            .setContentText("${formatted}")
//                            .setContentIntent(contentPendingIntent)
//                            .setPriority(NotificationCompat.PRIORITY_HIGH)
//                            .setAutoCancel(true)
//                            .setDefaults(NotificationCompat.DEFAULT_ALL)
//
//            notificationManager.notify(DailyAlarm.NOTIFICATION_ID, builder.build())
//        }



        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context) // 위치 정보 받기

        if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("hoihoihoi", "return")
            return
        }
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        lat = location.latitude
                        log = location.longitude
//                        Log.d("hoihoilatlat", lat.toString())
//                        Toast.makeText(context, "${lat} // ${log} 현재 위치 받아오기", Toast.LENGTH_LONG).show()


                        var X = 0.0
                        var Y = 0.0
                        var LOCATION = ""
                        val docRef10 = db.collection("Member").document(uid.toString()) // 여러 field값 가져오기
                        docRef10.get()
                                .addOnSuccessListener { document7 ->
                                    if (document7 != null) {
//                                        Log.d("hoihoihoi", "asdfasdf: ${document7.data}")
                                        //textViewName.setText(document.data?.get("name").toString()) // name 확인용
                                        X = (document7.data?.get("x") as Double)
                                        Y = (document7.data?.get("y") as Double)
                                        LOCATION = (document7.data?.get("location") as String)


                                        // 저장된 location 받아오기

//                        Toast.makeText(context, "${X} // ${Y} 저장된 위치 받아오기", Toast.LENGTH_LONG).show()

                                        Log.d("hoihoiXX", X.toString())
                                        Log.d("hoihoi현재", log.toString())

                                        // 위치 차이 계산

                                        if ((log - X < 0.005) && (log - X > -0.005)) {
                                            if ((lat - Y < 0.005) && (lat - Y > -0.005)) {
                                                val builder = /////////////////////////////////////////////////////////////alarm
                                                        NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                                                                .setSmallIcon(R.drawable.familyship)
                                                                .setContentTitle("누군가가 나의 위치 주변을 언급했어요")
                                                                .setContentText("${log.toString() + "/" + lat.toString()}")
                                                                .setContentIntent(contentPendingIntent)
                                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                                .setAutoCancel(true)
                                                                .setDefaults(NotificationCompat.DEFAULT_ALL)

                                                notificationManager.notify(NOTIFICATION_ID, builder.build())
                                            }
                                        }
                                    }
                                }
                    }
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
