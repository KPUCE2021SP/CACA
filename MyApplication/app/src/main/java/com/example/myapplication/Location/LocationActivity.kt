package com.example.myapplication.Location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.mypage_activity.*
import java.util.*
import kotlin.concurrent.*



class LocationActivity : AppCompatActivity() {

    var fname : String  = ""
    var str : String = ""

    var lat : Double = 1.0
    var log : Double = 1.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationManager : LocationManager? = null

    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)


        var L : String = ""


        val permissionListener = object : PermissionListener { // 위치 정보 권한 허가
            override fun onPermissionGranted() {
                Toast.makeText(this@LocationActivity, "권한 허가", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                Toast.makeText(this@LocationActivity, "권한 거부", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleConfirmText("권한 필요")
                .setDeniedMessage("권한 거절")
                .setPermissions(ACCESS_FINE_LOCATION)
                .check()


//
//        var n = 100
//        while(n>0){

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this) // 위치 정보 받기

            if (ActivityCompat.checkSelfPermission(
                            this,
                            ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            ACCESS_COARSE_LOCATION
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
                    .addOnSuccessListener { location : Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lat = location.latitude
                            log = location.longitude


                            var asdf : String = "위도 : " + lat.toString() + " 경도 : " + log.toString()
                            L_textView.setText(asdf)
                            Log.d("logD", asdf)
                        }
                    }

//            n -= 1
            lat = 0.0
            log = 0.0






        hey.setOnRefreshListener { // 새로고침
            // 사용자가 아래로 드래그 했다가 놓았을 때 호출 됩니다.
            // 이때 새로고침 화살표가 계속 돌아갑니다.

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this) // 위치 정보 받기

            if (ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@setOnRefreshListener
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        lat = location.latitude
                        log = location.longitude


                        var asdf : String = "위도 : " + lat.toString() + " 경도 : " + log.toString()
                        L_textView.setText(asdf)
                        Log.d("logD", asdf)
                    }
                }

//            n -= 1
            lat = 0.0
            log = 0.0


            hey.isRefreshing = false // 인터넷 끊기



        }


    }




}