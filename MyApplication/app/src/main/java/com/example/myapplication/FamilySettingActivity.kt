package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
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



//        binding = ActivityStorageBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        Firebase.auth.currentUser ?: finish() // if not authenticated, finish this activity
        storage = Firebase.storage

        button.setOnClickListener(){
            uploadDialog()
        }

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
                }catch(e:Exception){
                    e.printStackTrace()
                }
            }else{
                Log.d("FamilyImageView", "something wrong")
            }
        }
    }







    private fun uploadDialog() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null)

            AlertDialog.Builder(this)
                .setTitle("Choose Photo")
                .setCursor(cursor, { _, i ->
                    cursor?.moveToPosition(i)
                    uploadFile(cursor?.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)),
                        cursor?.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)))
                }, MediaStore.Images.ImageColumns.DISPLAY_NAME).create().show()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    private fun listPhotosDialog() {
        storage.reference.child(UPLOAD_FOLDER).listAll()
            .addOnSuccessListener {
                val itemsString = mutableListOf<String>()
                for (i in it.items) {
                    itemsString.add(i.name)
                }
                AlertDialog.Builder(this)
                    .setTitle("Uploaded Photos")
                    .setItems(itemsString.toTypedArray(), {_, i -> }).show()
            }.addOnFailureListener {

            }
    }

    private fun uploadFile(file_id: Long?, fileName: String?) {
        file_id ?: return
        val imageRef : StorageReference? = storage.reference.child("${UPLOAD_FOLDER}${fileName}")
        val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, file_id)
        imageRef?.putFile(contentUri)?.addOnCompleteListener {
            if (it.isSuccessful) {
                // upload success
                var t1 = Toast.makeText(this, "Upload completed.", Toast.LENGTH_SHORT)
                t1.show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                uploadDialog()
            }
        }
    }

    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) {
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
        }
    }

}