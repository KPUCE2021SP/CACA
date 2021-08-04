package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_familysetting.*

class FamilySettingActivity : AppCompatActivity() {
    private val OPEN_GALLERY = 1

//    lateinit var storage: FirebaseStorage
//    lateinit var binding: ActivityStorageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_familysetting)

        FamilyImageView.setOnClickListener(){ // 버튼 누르면 갤러리 접근
            openGallery()
        }


//        binding = ActivityStorageBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        Firebase.auth.currentUser ?: finish() // if not authenticated, finish this activity
//        storage = Firebase.storage
//
//        button.setOnClickListener(){
//            uploadDialog()
//        }

    }

//    private fun uploadDialog() {
//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//            == PackageManager.PERMISSION_GRANTED) {
//            val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, null, null, null)
//
//            AlertDialog.Builder(this)
//                .setTitle("Choose Photo")
//                .setCursor(cursor, { _, i ->
//                    cursor?.moveToPosition(i)
//                    uploadFile(cursor?.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)),
//                        cursor?.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)))
//                }, MediaStore.Images.ImageColumns.DISPLAY_NAME).create().show()
//        } else {
//            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
//        }
//    }
//
//
//    private fun uploadFile(file_id: Long?, fileName: String?) { // image file upload
//        file_id ?: return
//        val contentUri = ContentUris.withAppendedId(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, file_id)
//        val imageRef : StorageReference? = storage.reference.child("업로드 경로/파일명")
//        imageRef?.putFile(contentUri)?.addOnCompleteListener {
//            if (it.isSuccessful) {
//// upload success
//                Snackbar.make(binding.root, "Upload completed.", Snackbar.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE) {
//            if ((grantResults.isNotEmpty() &&
//                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                uploadDialog()
//            }
//        }
//    }
//
//
//
//
    private fun openGallery(){ // intent로 갤러리 앱 접근
        val intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)

    }
//
//
//    @Override
//    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?){ // Gallery
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(resultCode == Activity.RESULT_OK){
//            if(requestCode == OPEN_GALLERY){
//                var currentImageUrl : Uri? = data?.data
//
//                try{
//                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
//                    FamilyImageView.setImageBitmap(bitmap)
//                }catch(e:Exception){
//                    e.printStackTrace()
//                }
//            }else{
//                Log.d("FamilyImageView", "something wrong")
//            }
//        }
//    }

}