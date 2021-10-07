package com.example.myapplication.Location

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import com.example.myapplication.FamilySet.MainActivity
import com.example.myapplication.R
import com.example.myapplication.SerachMap.db
import com.example.myapplication.SerachMap.uid
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.android.synthetic.main.activity_home.*
import com.google.firebase.storage.ktx.storage

class AppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
    ) {
        // Perform this loop procedure for each App Widget that belongs to this provider
        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity
            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
                    .let { intent ->
                        PendingIntent.getActivity(context, 0, intent, 0)
                    }

            val storage = Firebase.storage
            var FamilyName = "6kRxCJjo"

            var body: Bitmap
            var acc: Bitmap
            var eye: Bitmap

            db.collection("Chats").document(FamilyName.toString()).collection("CUSTOM")
                    .document(FamilyName.toString())
                    .get()
                    .addOnSuccessListener { docName ->
                        if (docName != null) {
                            var body_color = docName.data?.get("bodyColor").toString()
                            val bodyName =
                                    "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
                            val customRef_body = storage.getReferenceFromUrl(bodyName)
                            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                                body = customRef
                                val views1: RemoteViews = RemoteViews(context?.packageName, R.layout.appwidget).apply {
                                    setImageViewBitmap(R.id.cu_body_Iv, body)
                                }
                                appWidgetManager!!.updateAppWidget(appWidgetId, views1)
                            }?.addOnFailureListener {
                                //   Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                            }

                            var custom_acc = docName.data?.get("customAcc").toString()
                            val accName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
                            val customRef_acc = storage.getReferenceFromUrl(accName)
                            customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                                acc = customRef
                                val views2: RemoteViews = RemoteViews(context?.packageName, R.layout.appwidget).apply {
                                    setImageViewBitmap(R.id.cu_acc_Iv, acc)
                                }
                                appWidgetManager!!.updateAppWidget(appWidgetId, views2)
                            }?.addOnFailureListener {
                                //    Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                            }


                            var custom_eye = docName.data?.get("customEye").toString()
                            val eyeName =
                                    "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_eye
                            val customRef_eye = storage.getReferenceFromUrl(eyeName)
                            customRef_eye?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                                eye = customRef
                                val views3: RemoteViews = RemoteViews(context?.packageName, R.layout.appwidget).apply {
                                    setImageViewBitmap(R.id.cu_eye_Iv, eye)
                                }
                                appWidgetManager!!.updateAppWidget(appWidgetId, views3)
                            }?.addOnFailureListener {
                                //     Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
        }
    }


        override fun onAppWidgetOptionsChanged(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle?) {
            super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)

            val storage = Firebase.storage
            var FamilyName = "6kRxCJjo"

            var body: Bitmap
            var acc: Bitmap
            var eye: Bitmap

            db.collection("Chats").document(FamilyName.toString()).collection("CUSTOM")
                    .document(FamilyName.toString())
                    .get()
                    .addOnSuccessListener { docName ->
                        if (docName != null) {
                            var body_color = docName.data?.get("bodyColor").toString()
                            val bodyName =
                                    "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
                            val customRef_body = storage.getReferenceFromUrl(bodyName)
                            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                                body = customRef
                                val views1: RemoteViews = RemoteViews(context?.packageName, R.layout.appwidget).apply {
                                    setImageViewBitmap(R.id.cu_body_Iv, body)
                                }
                                appWidgetManager!!.updateAppWidget(appWidgetId, views1)
                            }?.addOnFailureListener {
                                //   Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                            }

                            var custom_acc = docName.data?.get("customAcc").toString()
                            val accName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
                            val customRef_acc = storage.getReferenceFromUrl(accName)
                            customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                                acc = customRef
                                val views2: RemoteViews = RemoteViews(context?.packageName, R.layout.appwidget).apply {
                                    setImageViewBitmap(R.id.cu_acc_Iv, acc)
                                }
                                appWidgetManager!!.updateAppWidget(appWidgetId, views2)
                            }?.addOnFailureListener {
                                //    Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                            }


                            var custom_eye = docName.data?.get("customEye").toString()
                            val eyeName =
                                    "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_eye
                            val customRef_eye = storage.getReferenceFromUrl(eyeName)
                            customRef_eye?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                                eye = customRef
                                val views3: RemoteViews = RemoteViews(context?.packageName, R.layout.appwidget).apply {
                                    setImageViewBitmap(R.id.cu_eye_Iv, eye)
                                }
                                appWidgetManager!!.updateAppWidget(appWidgetId, views3)
                            }?.addOnFailureListener {
                                //     Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
        }


    //    //모든 브로드캐스트에서 위의 각 콜백 메서드 !!이전!!에 호출됩니다. 기본 AppWidgetProvider 구현은 모든 앱 위젯 브로드캐스트를 필터링하고 위 메서드를 적절하게 호출하므로 일반적으로 이 메서드를 구현할 필요가 없습니다.
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }



    // 앱 위젯이 앱 위젯 호스트에서 삭제될 때마다 호출됩니다.
    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }

    // 앱 위젯의 인스턴스가 처음으로 생성될 때 호출됩니다.
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    // 앱 위젯의 마지막 인스턴스가 앱 위젯 호스트에서 삭제될 때 호출됩니다.
    override fun onDisabled(context: Context?) {
        super.onDisabled(context)

    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
    }
}