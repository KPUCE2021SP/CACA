package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_familysetting.*
import java.io.File

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

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == OPEN_GALLERY){
                var currentImageUrl = data?.data

                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                    FamilyImageView.setImageBitmap(bitmap)

                    val path = intent.data?.path
                    var file = Uri.fromFile(File(path))
                    val storageRef = storage.reference
                    val riversRef = storageRef.child("images/${file.lastPathSegment}")
                    var uploadTask = riversRef.putFile(file)

// Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener {
                        // Handle unsuccessful uploads
                    }.addOnSuccessListener { taskSnapshot ->
                        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                        // ...
                    }




                }catch(e:Exception){
                    e.printStackTrace()
                }
            }else{
                Log.d("FamilyImageView", "something wrong")
            }
        }
    }



}