package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_familysetting.*

class FamilySettingActivity : AppCompatActivity() {
    private val OPEN_GALLERY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_familysetting)

        FamilyImageUpload.setOnClickListener(){ // 버튼 누르면 갤러리 접근
            openGallery()
        }


    }

    private fun openGallery(){ // intent로 갤러리 앱 접근
        val intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)

    }

}


@Override
override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?){
    super.onActivityResult(requestCode, resultCode, data)

    if(resultCode == Activity.RESULT_OK){
        if(requestCode == OPEN_GALLERY){
            var currentImageUrl : Url? = data?.data

            try{
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                FamilyImageView.setImageBitmap(bitmap)
            }catch(e:Exception){
                e.printStackTrace()
            }
        }else{
            Log.d("FamilyImageView", "something wrong")
        }
    }
}