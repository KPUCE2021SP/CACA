package com.example.myapplication

import androidx.appcompat.widget.ResourceManagerInternal.get
import com.example.myapplication.ApiClient
//import com.example.myapplication.ApiInterface
import com.example.myapplication.FcmInterface
import com.google.firebase.inappmessaging.dagger.Module
import io.reactivex.internal.util.QueueDrainHelper.request
import io.reactivex.schedulers.Schedulers.single
//import com.example.myapplication.FCM_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


val apiModule: org.koin.core.module.Module = module {

    val FCM_KEY = "AAAAP5mqhBY:APA91bH-Kw1glRwyPT5sOufJWqsV_b2p9QHgTHFFAPEbK9Oejp5XEUmz9tebZh_DygBzgXrQsm6XdwNLTUIB7PZ7LohU59-iow5aX7y6oEMb5kAXY0hw4X5NUq5-rpe0YOPvmfCkupDi"


    single<FcmInterface>(named("fcm")) { get<Retrofit>(named("fcm")).create(FcmInterface::class.java) }

    single<Retrofit>(named("fcm")) {
        Retrofit.Builder()
            .baseUrl(ApiClient.FCM_URL)
            .client(get(named("fcm")))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }


//    single<OkHttpClient>(named("fcm")) {
//        OkHttpClient.Builder()
//            .run {
//                addInterceptor(get<Interceptor>(named("fcm")))
//                build()
//            }
//    }
//
//    single<Interceptor>(named("fcm")) {
//        Interceptor { chain ->
//            with(chain) {
//                val newRequest = request().newBuilder()
//                    .addHeader("Authorization", "key=$FCM_KEY")
//                    .addHeader("Content-Type", "application/json")
//                    .build()
//                proceed(newRequest)
//            }
//        }
//    }

}
