package com.example.myapplication.Home_Board

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.SerachMap.SearchMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.board.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//edit_board_content



class BoardActivity : AppCompatActivity() {
    var fbAuth = FirebaseAuth.getInstance() // 로그인
    var mutableList: MutableList<String> = mutableListOf("a")
    var uid = fbAuth?.uid.toString() // uid
    private var storageReference: StorageReference? = null
    var mutableuidList: MutableList<String> = mutableListOf("null")
    var mentionPerson : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
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



        boardUpload.setOnClickListener(){ // 게시판 글 업로드하기
            var message = edit_board_content.text.toString()
            val current = LocalDateTime.now() // 글 작성한 시간 가져오기
            val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")
            val formatted = current.format(formatter)
            val board_content = hashMapOf( // Family name
                    "contents" to message,
                    "uid" to uid,
                    "time" to formatted,
                    "timeStamp" to current,
                    "mention" to mentionPerson,
            )

            db.collection("Chats").document(FamilyName.toString()).collection("BOARD").document(formatted).set(board_content) // 게시판 활성화
            Toast.makeText(this, "게시판 업로드 완료!!", Toast.LENGTH_SHORT).show()


            //게시판으로 돌아가기기
//            LinearMainage.visibility = View.GONE
//            LinearMain1.visibility = View.GONE
//            LinearMain2.visibility = View.VISIBLE
//            LinearMain3.visibility = View.GONE
//            LinearMain4.visibility = View.GONE
//            val intent = Intent(application, MainPageActivity::class.java)
//            startActivity(intent)
            finish()
//            LinearMainpage.visibility = View.GONE
//            LinearMain1.visibility = View.GONE
//            LinearMain2.visibility = View.VISIBLE
//            LinearMain3.visibility = View.GONE
//            LinearMain4.visibility = View.GONE


        }













        mutableList.clear()
        mutableList.add("언급 안하기")
        mutableList.add("모두 언급하기")
        mutableuidList.add("all")
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
                                Log.d(ContentValues.TAG, "DocumentSnapshot data: ${docName.data}")
                                mutableuidList.add(document.id.toString())
                                mutableList.add(docName.data?.get("name").toString())
//                                Log.d("ddddddddddddddd", mutableList.toString())
                                var adapter: ArrayAdapter<String>
                                adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mutableList)
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

        //aaaaaaa.setText(mutableList.toString())////////TEST

        var v = mutableList.toTypedArray() // !!ADAPTER!!
//        var adapter: ArrayAdapter<String>
//        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, v)
//        spinner_member.adapter = adapter



        spinner_member.setSelection(0, false)
        spinner_member.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mentionPerson = mutableuidList[position]
                Log.d("mention", mentionPerson.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }



    }
}


