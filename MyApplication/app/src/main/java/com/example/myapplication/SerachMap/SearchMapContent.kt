package com.example.myapplication.SerachMap

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_search_map.*
import kotlinx.android.synthetic.main.activity_search_map_content.*
import net.daum.mf.map.api.MapView


class SearchMapContent : AppCompatActivity() {
    companion object {
        var BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 6f72cd8b31110c1031e1f004fb80c3c8"  // REST API 키
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_map_content)
        val LocationName = intent.getStringExtra("LocationInfo") // 제목 선정
        Log.d("locationname", LocationName.toString())

//        val mapView = MapView(this)
//        val mapViewContainer = map_view_detail
//        mapViewContainer.addView(mapView)


    }
}