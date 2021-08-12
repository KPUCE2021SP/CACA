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


    lateinit var FamilyName : String
    lateinit var formatted : String


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
//        board_imageView.setImageResource(0) //////////////////////////////////////////////////////////////////////////////////////// ImageView 비우기
        storageReference = FirebaseStorage.getInstance().reference
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board)


        var fbAuth = FirebaseAuth.getInstance() // 로그인
        var fbFire = FirebaseFirestore.getInstance()
        var uid = fbAuth?.uid.toString() // uid
        val db: FirebaseFirestore = Firebase.firestore


        var message = edit_board_content.text.toString()
        Log.d("message", message)


        FamilyName = intent.getStringExtra("FamilyName").toString() // 제목 선정
        formatted = intent.getStringExtra("formatted").toString()



        Log.d("formatted", formatted)
        aaaaaaa.setText(formatted)
        //FamilyNameTextView.text = FamilyName


//

        board_imageView.background = getResources().getDrawable(R.drawable.imageview_cornerround, null)
        board_imageView.setClipToOutline(true)


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



        boardLocation.setOnClickListener {
            val intent = Intent(application, SearchMap::class.java)
            intent.putExtra("FamilyName", FamilyName)
            intent.putExtra("formatted", formatted)
            startActivity(intent)
//            location = intent.getStringExtra("resultList").toString() // SearchMap Activity 에서 돌아오면 실행하는 코드
//            board_location_textView.text = location
        }

        // location Text
        val docRef1 = db.collection("Chats").document(FamilyName.toString()).collection("BOARD").document(aaaaaaa.text.toString()) // 여러 field값 가져오기
        docRef1.get()
                .addOnSuccessListener { document2 ->
                    if (document2 != null) {
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document2.data}")
                        //textViewName.setText(document.data?.get("name").toString()) // name 확인용
                        board_location_textView.setText(document2.data?.get("location").toString())
                    }
                }


        boardVote.setOnClickListener {
            val intent = Intent(application, Notification::class.java)
            startActivity(intent)
        }

        mutableList.clear()
        mutableList.add("언급 안하기")
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

            var PhotoBoolean = false

            if (board_imageView.getDrawable() != null){ ///////////////////////// ImageView가 null이 아니라면 // 포토 사용
                uploadPhoto(FamilyName.toString(), formatted)
                PhotoBoolean = true
            }

            var message = edit_board_content.text.toString()

            val board_content = hashMapOf(
                // Family name
                "contents" to message,
                "uid" to uid,
                "time" to formatted,
                "mention" to spinnerUID.toString(),
                "photo" to PhotoBoolean,
            )

            val FFF= formatted.toString()
            db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                    .document(formatted).update(board_content as Map<String, Any>)//.set(board_content) // 게시판 활성화
            Toast.makeText(this, "게시판 업로드 완료!!", Toast.LENGTH_SHORT).show()

            Log.d("spinner", spinnerUID)

//            db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
//                .document(formatted).update(board_content as Map<String, Any>)//.set(board_content) // 게시판 활성화
//            Toast.makeText(this, "게시판 업로드 완료!!", Toast.LENGTH_SHORT).show()


            var no_mention: String = ""
            db.collection("Member").document(spinnerUID.toString()).collection("DEVICE").document("TOKEN")
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





//                        var x = 0.0f
//                        var y = 0.0f
//                        var location : String = ""
//                        val docRef4 = db.collection("Chats").document(FamilyName).collection("BOARD").document(formatted)
//                        docRef4.get()
//                                .addOnSuccessListener { dd ->
//                                    if (dd != null) {
//                                        Log.d(
//                                                ContentValues.TAG,
//                                                "aaaaaaaaaaaaaaaaaa: ${dd.data}"
//                                        )
//
//                                        x = dd.data?.get("x") as Float
//                                        y = dd.data?.get("y") as Float
//                                        location = dd.data?.get("location").toString()
//
//                                        Log.d("yyyyyy", x.toString())
//
//                                        val myPage_content = hashMapOf(
//                                                // Family name
//                                                "x" to x.toString(),
//                                                "y" to y.toString(),
//                                                "location" to location.toString(),
//                                        )
//
//
//                                        db.collection("Member").document(spinnerUID.toString()).update(myPage_content as Map<String, Any>) // mypage에 location정보 저장
//
//
//                                    } else {
//                                        Log.d(ContentValues.TAG, "No such document")
//                                    }
//                                }
//                                .addOnFailureListener { exception ->
//                                    Log.d(ContentValues.TAG, "get failed with ", exception)
//                                }
                    }
                }



            Log.d("format", FFF.toString())
            // x, y 받아오기///////////////////////////////////////////////////////////////////////////////////////////////////
            val docRef10 = db.collection("Chats").document(FamilyName.toString()).collection("BOARD").document(FFF) // 여러 field값 가져오기
            docRef10.get()
                    .addOnSuccessListener { document7 ->
                        if (document7 != null) {
                            Log.d("asdfasdf", "asdfasdf: ${document7.data}")
                            //textViewName.setText(document.data?.get("name").toString()) // name 확인용
                            var X = (document7.data?.get("x"))
                            var Y = (document7.data?.get("y"))
                            var Location = (document7.data?.get("location"))
                            Log.d("asdf", X.toString())

                        val myPage_content = hashMapOf(
//                                                // Family name
                                                "x" to X.toString(),
                                                "y" to Y.toString(),
                                                "location" to Location.toString(),
                                        )


                        db.collection("Member").document(spinnerUID.toString()).update(myPage_content as Map<String, Any>)
                        }
                    }


            //home으로 돌아가기기
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


    // 갤러리 접근\\
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


