package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_familysetting.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class FamilySettingActivity : AppCompatActivity() {
    private val OPEN_GALLERY = 1

    lateinit var storage: FirebaseStorage
//    lateinit var binding: ActivityStorageBinding

    companion object {
        const val REQUEST_CODE = 1
        const val UPLOAD_FOLDER = "upload_images/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_familysetting)

        FamilyImageView.setOnClickListener(){ // 버튼 누르면 갤러리 접근
            openGallery()
        }

        storage = Firebase.storage

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


                    var sdPath = Environment.getExternalStorageDirectory().absolutePath		//sd카드 절대 경로
                    var myDirPath = File("$sdPath/folderName")


                    saveBitmapAsFile(bitmap, sdPath)


                }catch (e: Exception){
                    e.printStackTrace()
                }
            }else{
                Log.d("FamilyImageView", "something wrong")
            }
        }
    }

    private fun saveBitmapAsFile(bitmap: Bitmap, filepath: String) {
        val file = File(filepath)
        var os: OutputStream? = null
        try {
            file.createNewFile()
            os = FileOutputStream(file)
            bitmap.compress(CompressFormat.PNG, 100, os)
            os.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}