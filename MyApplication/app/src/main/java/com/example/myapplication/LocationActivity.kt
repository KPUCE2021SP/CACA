package com.example.myapplication

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


class LocationActivity : AppCompatActivity() {

    var fname : String  = ""
    var str : String = ""

    var lat : Double = 1.0
    var log : Double = 1.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationManager : LocationManager? = null



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
                    Log.d("logD", asdf)
                }
            }




        L_Btn.setOnClickListener {////////////////////////////////////////////////////////////////////////////// 위치 불러오기 버튼

            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(this, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                lat,
                log,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()





            L_textView.setText(address)
            editTextTextMultiLine.setText(textView2.getText().toString()) // 저장된 내용 불러오기

            if(editTextTextMultiLine.getText().toString()==""){ // 아무것도 없다면 줄바꿈 없음
                var a : String = editTextTextMultiLine.getText().toString() + address
                editTextTextMultiLine.setText(a) // editText에 위치정보 추가하기
            }else{
                var a : String = editTextTextMultiLine.getText().toString() + "\n" + address
                editTextTextMultiLine.setText(a) // editText에 위치정보 추가하기
            }

        }






        @SuppressLint("WrongConstant")
        fun saveDiary(readyDay: String) {/////////////////////////////////////////////////////저장
            var fos: FileOutputStream? = null

            try {
                fos = openFileOutput(readyDay, Context.MODE_PRIVATE)

                var content: String = editTextTextMultiLine.getText().toString()
                fos.write(content.toByteArray())
                fos.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        @SuppressLint("WrongConstant")
        fun removeDiary(readyDay: String) {/////////////////////////////////////////////////////삭제
            var fas: FileOutputStream? = null

            try {
                fas = openFileOutput(readyDay, Context.MODE_PRIVATE)
                var content: String = ""
                fas.write(content.toByteArray())
                fas.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun checkedDay (cYear : Int, cMonth : Int, cDay : Int) {
            fname = "" + cYear + "-" + (cMonth + 1) + "-" + cDay + ".txt"

            var fis: FileInputStream? = null

            try {
                fis = openFileInput(fname)
                val fileData = ByteArray(fis.available())
                fis.read(fileData)
                fis.close()

                str = String(fileData)
                textView2.text = "${str}"
                textView2.setText("${str}")

                cha_Btn.setOnClickListener {// 수정 버튼
                    editTextTextMultiLine.setText(str)
                    textView2.text = "${editTextTextMultiLine.getText()}"
                }

                del_Btn.setOnClickListener {// 삭제 버튼
                    editTextTextMultiLine.setText("")
                    removeDiary(fname)
                    var t1 = Toast.makeText(this, fname + "데이터를 삭제했습니다.", Toast.LENGTH_SHORT)
                    t1.show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            false
        }

        calendarView.setOnDateChangeListener{view, year, month, dayOfMonth -> // 날자 바꾸기

            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            textView2.setText("")
            checkedDay(year, month, dayOfMonth)
        }



        save_Btn.setOnClickListener{// save 버튼
            saveDiary(fname)
            var t1 = Toast.makeText(this, fname + "데이터를 저장했습니다.", Toast.LENGTH_SHORT)
            t1.show()
            str = textView2.getText().toString()
            textView2.text = "${str}"
            textView2.setText("${str}")
        }
    }
}