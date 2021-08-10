package com.example.myapplication.Notification

import com.example.myapplication.Notification.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {

    companion object {
        var retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        val api =  retrofit.create(NotificationAPI::class.java)

    }
}