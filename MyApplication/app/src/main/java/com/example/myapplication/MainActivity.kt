package com.example.myapplication

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.ImageBtn
import kotlinx.android.synthetic.main.activity_main.iv1
import kotlinx.android.synthetic.main.loginactivity.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val storage = Firebase.storage
//        val remoteConfig = Firebase.remoteConfig
//
//        val configSettings = remoteConfigSettings {
//            minimumFetchIntervalInSeconds = 1 // 요청 시간 간격
//        }
//        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
//        remoteConfig.fetchAndActivate()
//                .addOnCompleteListener(this) {
//                    val your_price = remoteConfig.getLong("your_price")
//                    val cheat_enabled = remoteConfig.getBoolean("cheat_enabled")
//                    tv1.text = your_price.toString()
//                    tv2.text = cheat_enabled.toString()
//                }
//
//        Firebase.auth.currentUser ?:finish()
//        SignOut.setOnClickListener {
//            Firebase.auth.signOut()
//            startActivity(Intent(this, LoginActivity::class.java))
//        }

        val storage = Firebase.storage
        ImageBtn.setOnClickListener {
            val root_ref = storage.reference
            val img_ref =
                    storage.getReferenceFromUrl("gs://kpufirebasetest.appspot.com/images/kr_lotto.png")
            img_ref?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                iv1.setImageBitmap(bmp)
            }?.addOnFailureListener {
                Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
        }

        val db: FirebaseFirestore = Firebase.firestore
        val itemsCollectionRef = db.collection("items")

//    }
//    private fun accessFirebase(){
//        val db: firebaseFireStore = Firebase.firestore
//        //val itemsCollectionRef = db.collection("items")
//        val itemsRef= firestore.collection("items")
//        val itemMap = hashMapOf(
//                "name" to "Jiyoung",
//                "price" to 100
//        )
//        itemsRef.add(itemMap)
//    }
    }
}