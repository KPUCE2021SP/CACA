package com.example.myapplication

import android.app.AlertDialog
import android.content.ContentUris
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_familysetting.*
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast


class FamilySettingActivity : AppCompatActivity() {
    private val OPEN_GALLERY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_familysetting)

        FamilyImageView.setOnClickListener {
            openGallery()
        }
    }



    private fun openGallery(){ // intent로 갤러리 앱 접근
        val intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == OPEN_GALLERY){
                var currentImageUrl = data?.data

                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                    FamilyImageView.setImageBitmap(bitmap) // imageView에 표시

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }else{
                Log.d("FamilyImageView", "something wrong")
            }
        }
    }

}