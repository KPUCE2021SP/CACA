package com.example.myapplication

import com.example.myapplication.Constants.Companion.BASE_URL
import com.example.myapplication.NotificationAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {

        private val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        val api = retrofit.create(NotificationAPI::class.java)



//        private val retrofit by lazy {
//            Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//        }
//
//        val api by lazy {
//            retrofit.create(NotificationAPI::class.java)
//        }
    }
}