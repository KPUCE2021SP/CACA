package com.example.myapplication.SerachMap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
                Log.d("Test", "Body: ${response.body()}")
                DummySearch.sDummyData = ""
                DummySearch.sDummyData = response.body().toString()
                    .replace("ResultSearchKeyword(documents=[","")
                    .replace("Place","")
                    .replace("(","{\"")
                    .replace("), ","\"},")
                    .replace("=","\":\"")
                    .replace(", ","\",\"")
                    .replace(")])","\"}")
//                    .replace("}, {", "},{")
                Log.d("dummy", DummySearch.sDummyData.toString())

                var mutableList : MutableList<String> = mutableListOf("a")
                mutableList.clear()

                val containView = layoutInflater.inflate(R.layout.searchmap_content, null)
                val ContentView = containView as View
                var place_name = ContentView.findViewById(R.id.place_name) as TextView // 장소
                var address_name = ContentView.findViewById(R.id.address_name) as TextView // 주소
                var road_address_name = ContentView.findViewById(R.id.road_address_name) as TextView // 도로명주소
                var x = ContentView.findViewById(R.id.x) as TextView    //x
                var y = ContentView.findViewById(R.id.y) as TextView    //y
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("MainActivity", "통신 실패: ${t.message}")
            }
        })
    }


}

