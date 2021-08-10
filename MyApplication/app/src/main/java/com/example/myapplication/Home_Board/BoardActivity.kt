package com.example.myapplication.Home_Board

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Notification.Notification
import com.example.myapplication.Notification.NotificationData
import com.example.myapplication.Notification.PushNotification
import com.example.myapplication.Notification.RetrofitInstance
import com.example.myapplication.R
import com.example.myapplication.SerachMap.SearchMap
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.board.*
import kotlinx.android.synthetic.main.mypage_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.security.KeyStore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//edit_board_content



class BoardActivity : AppCompatActivity() {
    var fbAuth = FirebaseAuth.getInstance() // 로그인
    var mutableList: MutableList<String> = mutableListOf("a")
    var mutableUIDList: MutableList<String> = mutableListOf("null")
    var uid = fbAuth?.uid.toString() // uid

    var fbFire = FirebaseFirestore.getInstance()
    var uemail = fbAuth?.currentUser?.email.toString()

    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
//        board_imageView.setImageResource(0) //////////////////////////////////////////////////////////////////////////////////////// ImageView 비우기
        storageReference = FirebaseStorage.getInstance().reference
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board)


        //프로필사진 불러오기
        val imageName = "gs://cacafirebase-554ac.appspot.com/profiles/" + uid
        Log.d("imageName", imageName)
        val storage = Firebase.storage
        val storageRef = storage.reference
        val profileRef1 = storage.getReferenceFromUrl(imageName)
        profileRef1?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            board_Profile.setImageBitmap(profilebmp)
        }?.addOnFailureListener {
            Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
        }


        val FamilyName = intent.getStringExtra("FamilyName") // 제목 선정
        //FamilyNameTextView.text = FamilyName


        var fbAuth = FirebaseAuth.getInstance() // 로그인
        var fbFire = FirebaseFirestore.getInstance()
        var uid = fbAuth?.uid.toString() // uid
        val db: FirebaseFirestore = Firebase.firestore


        boardLocation.setOnClickListener {
            val intent = Intent(application, SearchMap::class.java)
            startActivity(intent)
        }

        boardVote.setOnClickListener {
            val intent = Intent(application, Notification::class.java)
            startActivity(intent)
        }

        mutableList.clear()
        mutableList.add("언급 안하기")
//        mutableList.add("모두 언급하기")
//        mutableUIDList.add("all")
        db.collection("Chats").document(FamilyName.toString()).collection("FamilyMember")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("FamilyMember", "${document.id}")

//                    //Member 이름 가져와서 mutableNameList에 저장
                    val docRef2 = db.collection("Member").document(document.id)
                    docRef2.get()
                        .addOnSuccessListener { docName ->
                            if (docName != null) {
                                Log.d(
                                    ContentValues.TAG,
                                    "DocumentSnapshot data: ${docName.data}"
                                )
                                mutableUIDList.add(document.id.toString())
                                mutableList.add(docName.data?.get("name").toString())


                                var adapter: ArrayAdapter<String>
                                adapter = ArrayAdapter(
                                    this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    mutableList
                                )
                                spinner_member.adapter = adapter


                            } else {
                                Log.d(ContentValues.TAG, "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }
//                    }

                }

            }



        // 사진 업로드하기
        boradAddPic.setOnClickListener(){
            ImagePicker()
        }



        // 언급하기 스피너
        var spinnerUID: String = ""
        spinner_member.setSelection(0, false)
        spinner_member.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                spinnerUID = mutableUIDList[position].toString()
                Log.d("spinnerUID", spinnerUID)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        boardUpload.setOnClickListener() { // 게시판 글 업로드하기
            var message = edit_board_content.text.toString()
            val current = LocalDateTime.now() // 글 작성한 시간 가져오기
            val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")
            val formatted = current.format(formatter)
            var PhotoBoolean : Boolean = false // 사진 사용 여부

            if (board_imageView.getDrawable() != null){ ///////////////////////// ImageView가 null이 아니라면 // 포토 사용
                uploadPhoto(FamilyName.toString(), formatted)
                PhotoBoolean = true
            }

            val board_content = hashMapOf(
                // Family name
                "contents" to message,
                "uid" to uid,
                "time" to formatted,
                "mention" to spinnerUID.toString(),
                "photo" to PhotoBoolean
            )

            Log.d("spinner", spinnerUID)

            db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                .document(formatted).set(board_content) // 게시판 활성화
            Toast.makeText(this, "게시판 업로드 완료!!", Toast.LENGTH_SHORT).show()


            var no_mention: String = ""
            db.collection("Member").document(spinnerUID).collection("DEVICE").document("TOKEN")
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        no_mention = document.data?.get("deviceInfo").toString()
                        Log.d("recipientToken", no_mention.toString())
                        //알람 울리기
                        val title = "게시판에서 누군가 언급하였습니다!"
                        val content = message.toString()
                        val recipientToken = no_mention
                        Log.d("recipientToken", recipientToken.toString())
                        if (title != "" && content != "" && recipientToken != "") {
                            PushNotification(
                                NotificationData(title, content),
                                recipientToken
                            )
                            sendNotification(
                                PushNotification(
                                    NotificationData(title, message),
                                    recipientToken
                                )
                            )
                        }
                    }
                }
            //게시판으로 돌아가기기
            finish()
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


    // 갤러리 접근
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
                board_imageView.setImageBitmap(bitmap)/////////////////////////////////////////////////////////////////////////// 사진 고르면 ImageView에 적용
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadPhoto(FamilyName : String, uploadPhoto : String){ // fireStore upload
        val uidText : String = uid
        if(filePath != null){
            val ref = storageReference?.child("Family_Board/" + FamilyName + "_" + uploadPhoto)
            ref?.putFile(filePath!!)?.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
                Toast.makeText(applicationContext, "Board Image Uploaded", Toast.LENGTH_SHORT).show()
            })?.addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(applicationContext, "Board Image Uploading Failed " + e.message, Toast.LENGTH_SHORT).show()
            })
        }else{
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }
    }








    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d(TAG, "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
}