package com.example.myapplication.Home_Board

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ContentView
import androidx.core.view.isGone
import com.example.myapplication.R
import com.example.myapplication.SerachMap.db
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_board_content.*
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.notice_card.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BoardContentActivity : AppCompatActivity() {
    var fbAuth = FirebaseAuth.getInstance()
    var uid = fbAuth?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_content)

        var formatted1 : String = "asdf"

        var intent = intent

        var FamilyName1 = intent.getStringExtra("FamilyNameB") // 제목 선정
        var notice_board1 = intent.getStringExtra("notice_boardB") // 제목 선정
        var notice_name1 = intent.getStringExtra("notice_nameB") // 제목 선정
        var notice_profile1 = intent.getStringExtra("notice_profileB") // 제목 선정
        var BoardImageName1 = intent.getStringExtra("notice_imageB")


        //// profile Image
        val storage = Firebase.storage
        val storageRef = storage.reference
        val profileRef1 = notice_profile1?.let { storage.getReferenceFromUrl(it) }
        profileRef1?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            notice_profileB.setImageBitmap(profilebmp) // 작성한 사람 uid로 profileImage 변경!
        }?.addOnFailureListener {
//            Toast.makeText(
//                this,
//                "image downloade failed",
//                Toast.LENGTH_SHORT
//            ).show()
        }

        //// Board Image
        val storage2 = Firebase.storage
        val storageRef2 = storage.reference
        val profileRef2 = BoardImageName1?.let { storage.getReferenceFromUrl(it) }
        profileRef2?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val profilebmp2 = BitmapFactory.decodeByteArray(it, 0, it.size)
            notice_imageB.setImageBitmap(profilebmp2) // 작성한 사람 uid로 profileImage 변경!
        }?.addOnFailureListener {
//            Toast.makeText(
//                this,
//                "image downloade failed",
//                Toast.LENGTH_SHORT
//            ).show()
            notice_imageB.isGone =
                true                            // 업로드된 이미지가 없다면
        }

        formatted1 = intent.getStringExtra("formattedB").toString()
        if (formatted1 != null) {
            Log.d("Fmt",formatted1)
        }

        notice_boardB.text = notice_board1
        notice_nameB.text = notice_name1
        notice_timeB.text = formatted1

        notice_imageB.background =
            getResources().getDrawable(R.drawable.imageview_cornerround, null)
        notice_imageB.setClipToOutline(true)

        if (FamilyName1 != null) {  //댓글 가져오기
            commentRe(FamilyName1, formatted1)
        }

        srl_main.setOnRefreshListener {     //댓글 새로고침 하기
            if (FamilyName1 != null) {
                commentRe(FamilyName1, formatted1)
            }

            srl_main.isRefreshing = false // 인터넷 끊기
        }

        comment_Btn.setOnClickListener {    //댓글 저장하기
            if (FamilyName1 != null && notice_name1 != null) {
                saveComment(FamilyName1, formatted1, notice_name1)
            }
        }



    }

    fun commentRe(FamilyName: String, formatted : String){      //댓글 가져오기
        var mutableList: MutableList<String> = mutableListOf("a")
        mutableList.clear()
        comment_Layout.removeAllViews()
        db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
            .document(formatted).collection(formatted)
            .get()
            .addOnSuccessListener { documents ->
                for (document1 in documents) {
                    mutableList.add(document1.id.toString())
                }

                notice_comment2.setText(mutableList.size.toString())
                for (i in 0..(mutableList.size - 1)) { // 거꾸로
                    val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val containView = layoutInflater.inflate(R.layout.activity_comment,null) // mypage_content를 inflate
                    comment_Layout.addView(containView)

                    val ContentView = containView as View
                    var commentName = ContentView.findViewById(R.id.commentName) as TextView // 이름
                    var commentDate = ContentView.findViewById(R.id.commentDate) as TextView // 시간
                    var comment_content = ContentView.findViewById(R.id.comment_content) as TextView // 내용
                    var comment_profile = ContentView.findViewById(R.id.comment_profile) as ImageView // profile Image


                    db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                        .document(formatted).collection(formatted).document(mutableList[i].toString())
                        .get()
                        .addOnSuccessListener { document2 ->
                            if (document2 != null) {
                                commentName.setText(document2.data?.get("name").toString())
                                commentDate.setText(document2.data?.get("time").toString())
                                comment_content.setText(document2.data?.get("contents").toString())

                                val imageName =
                                    "gs://cacafirebase-554ac.appspot.com/profiles/" + document2.data?.get("uid").toString()
                                Log.d("imageName", imageName)
                                val storage = Firebase.storage
                                val storageRef = storage.reference
                                val profileRef1 = storage.getReferenceFromUrl(imageName)
                                profileRef1?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                    val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                                    comment_profile.setImageBitmap(profilebmp) // 작성한 사람 uid로 profileImage 변경!
                                }
                            }



                        }

                    comment_content?.setOnClickListener {
                        val dlg: AlertDialog.Builder = AlertDialog.Builder(
                            this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                        )
                        dlg.setTitle("항목 삭제") //제목
                        dlg.setMessage(comment_content.text.toString() + "을(를) 정말 삭제하시겠습니까?") // 메시지
                        dlg.setPositiveButton(
                            "확인",
                            DialogInterface.OnClickListener { dialog, which ->
                                // DB 삭제
                                var fbAuth = FirebaseAuth.getInstance()
                                val db: FirebaseFirestore = Firebase.firestore

                                val docRef = db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                                    .document(formatted).collection(formatted).document(commentDate.text.toString())
                                    .delete()

                                Toast.makeText(this, "댓글 삭제가 완료되었습니다", Toast.LENGTH_SHORT).show()

                            })
                        dlg.setNegativeButton(
                            "취소",
                            DialogInterface.OnClickListener { dialog, which ->
                                // 취소
                            })
                        dlg.show()
                    }
                }

            }




    }

    fun saveComment(FamilyName : String, formatted : String, userName : String){   //댓글 저장하기

        val current_comment = LocalDateTime.now() // 댓글 작성한 시간 가져오기
        val formatter_comment = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")
        val formatted_comment = current_comment.format(formatter_comment)

        val board_comment = hashMapOf(
            // Family name
            "contents" to comment_editText.text.toString(),
            "uid" to uid.toString(),
            "time" to formatted_comment.toString(),
            "name" to userName,
        )
        db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
            .document(formatted).collection(formatted).document(formatted_comment).set(board_comment as Map<String, Any>)
        Toast.makeText(this, "댓글 작성이 완료되었습니다", Toast.LENGTH_SHORT).show()

        comment_editText.text = null
    }

//    fun deleteComment(FamilyName : String, formatted : String, formatted_comment : String){
//        val dlg: AlertDialog.Builder = AlertDialog.Builder(
//            this,
//            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
//        )
//        dlg.setTitle("항목 삭제") //제목
//        dlg.setMessage("댓글을 정말 삭제하시겠습니까?") // 메시지
//        dlg.setPositiveButton(
//            "확인",
//            DialogInterface.OnClickListener { dialog, which ->
//                // DB 삭제
//                var fbAuth = FirebaseAuth.getInstance()
//                val db: FirebaseFirestore = Firebase.firestore
//
//                val docRef = db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
//                    .document(formatted).collection(formatted).document(formatted_comment)
//                    .delete()
//
//            })
//        dlg.setNegativeButton(
//            "취소",
//            DialogInterface.OnClickListener { dialog, which ->
//                // 취소
//            })
//        dlg.show()
//    }
}