package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_familysetting.*
import kotlinx.android.synthetic.main.board.*
import kotlinx.android.synthetic.main.signuppage.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import java.util.jar.Manifest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter




class FamilySettingActivity : AppCompatActivity() {
    private var imagePreview: ImageView? = null
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_familysetting)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference




        var code : String = ""

        // intent 넘겨서 들어왔을 경우 랜덤 코드 생성
        var characterTable = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0") // 랜덤 코드 배열

//        var code : String = ""
        for (i in 0..7) { // 랜덤 코드 8자리 생성
            val random = Random()
            val num = random.nextInt(characterTable.size)
            code += characterTable[num]
        }




        FamilyImageView.setOnClickListener { ImagePicker() }
        //button.setOnClickListener { uploadImage(code) }

        doneFamilyButton.setOnClickListener(){ // 가족 만들기 완료 버튼
            uploadImage(code)
        }
    }



    private fun ImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                FamilyImageView?.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(code : String){
        if(filePath != null){
            if (editTextFamilyname.text.toString() != "") {

                val ref = storageReference?.child("Family_Image/" + code) // image upload
                ref?.putFile(filePath!!)?.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
                    Toast.makeText(applicationContext, "Image Uploaded", Toast.LENGTH_SHORT).show()

                })?.addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(applicationContext, "Image Uploading Failed " + e.message, Toast.LENGTH_SHORT).show()
                })


                val family = hashMapOf( // Family name
                    "name" to editTextFamilyname.text.toString()
                )

                val db: FirebaseFirestore = Firebase.firestore
                var fbAuth = FirebaseAuth.getInstance() // 로그인
                var uid = fbAuth?.uid.toString() // uid
                var WelcomeMessage = "${editTextFamilyname.text.toString()}의 게시판이 활성화 되었습니다!"
                db.collection("Chats").document(code).set(family) // db에 넣기
                db.collection("Member").document(uid).collection("MYPAGE").document(code).set(family)



                ///////// 가족 게시판 활성화
                val current = LocalDateTime.now() // 글 작성한 시간 가져오기 ///////// 가족 게시판 활성화
                val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")
                val formatted = current.format(formatter)

                val board_content = hashMapOf( // Family name
                    "contents" to WelcomeMessage,
                    "uid" to uid,
                    "time" to formatted,
                    "timeStamp" to current
                )

                db.collection("Chats").document(code).collection("BOARD").document(formatted).set(board_content) // 게시판 활성화
                //db.collection("Chats").document(code).collection("BOARD").document(WelcomeMessage).set(family) // 게시판 활성화



                val intent = Intent(this, DynamicLinkActivity::class.java) // 가족 초대하기 페이지
                intent.putExtra("Random_Code", code)
                startActivity(intent)

            }else {
                Toast.makeText(this, "가족 이름을 입력하세요!", Toast.LENGTH_SHORT).show()
            }


        }else{
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }
    }
}