package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase

class DynamicLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)



        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://familyship.page.link/DynamicLinkActivity")
            domainUriPrefix = "https://familyship.page.link/DynamicLinkActivity"
            // Open links with this app on Android
            androidParameters { }
        }

        val dynamicLinkUri = dynamicLink.uri
    }

}