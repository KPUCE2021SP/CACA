// 위젯 안씀///////////////////////////////////////////////////////////////////////////////////////

//package com.example.myapplication
//
//import android.Manifest
//import android.app.AlarmManager
//import android.app.AlarmManager.ELAPSED_REALTIME
//import android.app.PendingIntent
//import android.appwidget.AppWidgetManager
//import android.appwidget.AppWidgetProvider
//import android.content.ContentValues
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.location.Location
//import android.location.LocationManager
//import android.os.Bundle
//import android.os.SystemClock
//import android.util.Log
//import android.widget.CompoundButton
//import android.widget.RemoteViews
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import com.example.myapplication.FamilySet.MainActivity
//import com.example.myapplication.Notification.NotificationData
//import com.example.myapplication.Notification.PushNotification
//import com.example.myapplication.Notification.RetrofitInstance
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.firebase.messaging.FirebaseMessaging
//import com.google.gson.Gson
//import kotlinx.android.synthetic.main.activity_location.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.util.*
//import com.example.myapplication.Notification.Notification
//import kotlinx.android.synthetic.main.activity_main.*
//import java.lang.Thread.sleep
//
//
//var fname : String  = ""
//var asdf : String = "호잇"
//
//var lat : Double = 1.0
//var log : Double = 1.0
//
//var token = ""
//
//lateinit var fusedLocationClient: FusedLocationProviderClient
//
//var locationManager : LocationManager? = null
//
//lateinit var locationCallback: LocationCallback
//
//lateinit var alarm : AlarmManager
//// 더 자주 업데이트해야 하거나 기기가 대기 상태인 동안 업데이트하지 않아도 되는 경우 기기의 대기 모드를 해제하지 않는 알람을 기반으로 업데이트를 대신 실행할 수 있습니다.
//// 그렇게 하려면 AlarmManager를 사용하여 AppWidgetProvider가 수신하는 인텐트로 알람을 설정하세요.
//// 알람 유형을 ELAPSED_REALTIME 또는 RTC로 설정하세요. 이렇게 하면 기기의 대기 모드가 해제된 경우에만 알람이 전달됩니다. 그런 다음 updatePeriodMillis를 "0"으로 설정하세요.
//
//
//
////class AppWidgetProvider : AppWidgetProvider() {
////
////    private var alarmMgr: AlarmManager? = null
////    private lateinit var alarmIntent: PendingIntent
////
////
////
////
////
////    //모든 브로드캐스트에서 위의 각 콜백 메서드 !!이전!!에 호출됩니다. 기본 AppWidgetProvider 구현은 모든 앱 위젯 브로드캐스트를 필터링하고 위 메서드를 적절하게 호출하므로 일반적으로 이 메서드를 구현할 필요가 없습니다.
////    override fun onReceive(context: Context?, intent: Intent?) {
////        super.onReceive(context, intent)
////
////
////
//////        if (context != null) {
//////            alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//////        }
//////        alarmIntent = Intent(context, com.example.myapplication.AppWidgetProvider::class.java).let { intent ->
//////            PendingIntent.getBroadcast(context, 0, intent, 0)
//////        }
//////
//////        var a = AlarmManager.RTC
//////
//////        alarmMgr?.setInexactRepeating(
//////                AlarmManager.ELAPSED_REALTIME_WAKEUP,
//////                SystemClock.elapsedRealtime() + AlarmManager.RTC,
//////                a.toLong(),
//////                alarmIntent
//////        )
////
////    }
////
////
////
////    // 위젯이 처음으로 배치될 때와 위젯의 크기가 조절될 때 호출됩니다.
////    override fun onAppWidgetOptionsChanged(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle?) {
////        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
////
////
////        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!) // 위치 정보 받기
////
////        if (ActivityCompat.checkSelfPermission(
////                        context,
////                        Manifest.permission.ACCESS_FINE_LOCATION
////                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
////                        context,
////                        Manifest.permission.ACCESS_COARSE_LOCATION
////                ) != PackageManager.PERMISSION_GRANTED
////        ) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
////            return
////        }
////        fusedLocationClient.lastLocation
////                .addOnSuccessListener { location: Location? ->
////                    // Got last known location. In some rare situations this can be null.
////                    if (location != null) {
////                        lat = location.latitude
////                        log = location.longitude
////
////
////                        asdf = "위도 : " + lat.toString() + " 경도 : " + log.toString()
////                        Log.d("asdf", asdf)
////                    }
////                }
////
////
////        val views: RemoteViews = RemoteViews(context.packageName, R.layout.appwidget).apply {
////            setTextViewText(R.id.appwidgetText, asdf)
////        }
////
////        // Tell the AppWidgetManager to perform an update on the current app widget
////        appWidgetManager!!.updateAppWidget(appWidgetId, views)
////
////
////
////    }
////
////
////
////
////
////    // 앱 위젯이 앱 위젯 호스트에서 삭제될 때마다 호출됩니다.
////    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
////        super.onDeleted(context, appWidgetIds)
////    }
////
////    // 앱 위젯의 인스턴스가 처음으로 생성될 때 호출됩니다.
////    override fun onEnabled(context: Context?) {
////        super.onEnabled(context)
////    }
////
////    // 앱 위젯의 마지막 인스턴스가 앱 위젯 호스트에서 삭제될 때 호출됩니다.
////    override fun onDisabled(context: Context?) {
////        super.onDisabled(context)
////    }
////
////    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
////        super.onRestored(context, oldWidgetIds, newWidgetIds)
////    }
////
////    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
////
////        // location
////
////
////        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context) // 위치 정보 받기
////
////        if (ActivityCompat.checkSelfPermission(
////                context,
////                Manifest.permission.ACCESS_FINE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
////                context,
////                Manifest.permission.ACCESS_COARSE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED
////        ) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
////            return
////        }
////        fusedLocationClient.lastLocation
////            .addOnSuccessListener { location: Location? ->
////                // Got last known location. In some rare situations this can be null.
////                if (location != null) {
////                    lat = location.latitude
////                    log = location.longitude
////
////
////                    asdf = "위도 : " + lat.toString() + " 경도 : " + log.toString()
////                    Log.d("asdf", asdf)
////                }
////            }
////
////        // location
////
////
////        // message
////
////        // token
////
////        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
////            if (!task.isSuccessful) {
////                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
////                return@OnCompleteListener
////            }
////
////            // Get new FCM registration token
////            token = task.result
////
////            // Log and toast
////            val msg = token.toString()
////
////        })
////
////
////        //알람 울리기 // 아직 동작 안함
////        val title = "누군가가 나의 근처 위치를 언급하였습니다!"
////        val content = "누르면 앱으로 이동".toString()
////        if (lat == lat && log == log) {
////            PushNotification(NotificationData(title, content), token)
////            Log.d("asdf", "o")
////        }else{
////            Log.d("asdf", "x")
////        }
////
////        // message
////
////
////        // Perform this loop procedure for each App Widget that belongs to this provider
////        appWidgetIds.forEach { appWidgetId ->
////            // Create an Intent to launch ExampleActivity
////            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
////                .let { intent ->
////                    PendingIntent.getActivity(context, 0, intent, 0)
////                }
////
////            asdf = "위도 : " + lat.toString() + " 경도 : " + log.toString()
////
////
////            // Get the layout for the App Widget and attach an on-click listener
////            // to the button
////             val views: RemoteViews = RemoteViews(context.packageName, R.layout.appwidget).apply {
////                setOnClickPendingIntent(R.id.btn, pendingIntent)
////                 setTextViewText(R.id.appwidgetText, asdf)
////            }
////
////            // Tell the AppWidgetManager to perform an update on the current app widget
////            appWidgetManager.updateAppWidget(appWidgetId, views)
////
////        }
////    }
////}