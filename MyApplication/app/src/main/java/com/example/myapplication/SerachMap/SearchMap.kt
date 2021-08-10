package com.example.myapplication.SerachMap

import android.Manifest
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_search_map.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

val PERMISSIONS_REQUEST_CODE = 100
var REQUIRED_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)

class SearchMap : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    companion object {
        var BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 6f72cd8b31110c1031e1f004fb80c3c8"  // REST API 키
    }

//    var nLatitude : String = ""
//    var nLongitude : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_map)

        val mapView = MapView(this)
        val mapViewContainer = map_view
        mapViewContainer.addView(mapView)
        //searchKeyword("은행")


        val gpsmarker = MapPOIItem()


        btn_gps.setOnClickListener { // 현재위치
            getHashKey()
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                val lm: LocationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
                try {
                    val userNowLocation: Location =
                        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                    val uLatitude = userNowLocation.latitude
                    val uLongitude = userNowLocation.longitude
                    val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)

                    mapView.setMapCenterPoint(uNowPosition, true)
                    gpsmarker.mapPoint = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
                    gpsmarker.itemName= "현재위치"
                    gpsmarker.tag = 0
                    gpsmarker.markerType = MapPOIItem.MarkerType.BluePin
                    gpsmarker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                    mapView.addPOIItem(gpsmarker)
                    var nLatitude = uLatitude
                    var nLongitude = uLongitude

                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener { // 검색
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            Log.d("Latitude", nLatitude.toString())
                            searchKeyword(query!!, nLatitude.toString(), nLongitude.toString())
//                            searchKeyword(query!!, "37.34554066939573", "126.7362295488324")

                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            return false
                        }
                    })




                } catch (e: NullPointerException) {
                    Log.e("LOCATION_ERROR", e.toString())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ActivityCompat.finishAffinity(this)
                    } else {
                        ActivityCompat.finishAffinity(this)
                    }

                    System.exit(0)
                }

            } else {
                Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }



        }
    }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String, x:String, y:String) {
        val retrofit = Retrofit.Builder()   // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java)   // 통신 인터페이스를 객체로 생성
//        val call = api.getSearchKeyword(API_KEY, keyword)   // 검색 조건 입력
        Log.d("Latitude", x.toString())
        val call = api.getSearchKeyword(API_KEY, keyword, y, x, 20000)
        // API 서버에 요청
        call.enqueue(object: Callback<ResultSearchKeyword> {
            override fun onResponse(
                    call: Call<ResultSearchKeyword>,
                    response: Response<ResultSearchKeyword>
            ) {
                // 통신 성공 (검색 결과는 response.body()에 담겨있음)
                Log.d("Test", "Raw: ${response.raw()}")
                Log.d("Test", "Body: ${response.body()}") // 이거 사용

                var result = response.body().toString()
                result = result.replace("ResultSearchKeyword(documents=[Place(", "")
                result = result.replace("), Place(", "*")
                result = result.replace(")])", "")

                DummySearch.sDummySearch = result



                makeList() // List
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("MainActivity", "통신 실패: ${t.message}")
            }
        })
    }


    fun getHashKey(){
        var packageInfo : PackageInfo = PackageInfo()
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }
        for (signature: Signature in packageInfo.signatures){
            try{
                var md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEY_HASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch(e: NoSuchAlgorithmException){
                Log.e("KEY_HASH", "Unable to get MessageDigest. signature = " + signature, e)
            }
        }
    }



    // list
    fun makeList() {
        var resultList =
            DummySearch.sDummySearch.trim().splitToSequence("*").toList() // String to List
        var resultArray: Array<String> = resultList.toTypedArray() // List to Array
        var myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resultArray)
        mapListView.adapter = myAdapter
        Log.d("Test", "List: ${resultList.toString()}")
    }

}

