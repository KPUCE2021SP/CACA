package com.example.myapplication

import android.Manifest
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.FamilySet.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_location.*
import java.util.*

class AppWidgetProvider : AppWidgetProvider() {

    var fname : String  = ""
    var asdf : String = "호잇"

    var lat : Double = 1.0
    var log : Double = 1.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationManager : LocationManager? = null

    private lateinit var locationCallback: LocationCallback

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        // location

        var lat: Double = 1.0
        var log: Double = 1.0

        var fusedLocationClient: FusedLocationProviderClient
        var locationManager: LocationManager? = null
        var locationCallback: LocationCallback


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
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    lat = location.latitude
                    log = location.longitude


                    asdf = "위도 : " + lat.toString() + " 경도 : " + log.toString()
                    Log.d("logD", asdf)
                }
            }

        // location


        // Perform this loop procedure for each App Widget that belongs to this provider
        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity
            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
                .let { intent ->
                    PendingIntent.getActivity(context, 0, intent, 0)
                }

            asdf = "위도 : " + lat.toString() + " 경도 : " + log.toString()


            // Get the layout for the App Widget and attach an on-click listener
            // to the button
             val views: RemoteViews = RemoteViews(context.packageName, R.layout.appwidget).apply {
                setOnClickPendingIntent(R.id.btn, pendingIntent)
                 setTextViewText(R.id.appwidgetText, asdf)
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)


        }
    }

}