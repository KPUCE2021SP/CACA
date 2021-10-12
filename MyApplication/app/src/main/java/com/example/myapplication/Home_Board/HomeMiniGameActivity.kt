package com.example.myapplication.Home_Board

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home_mini_game.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class HomeMiniGameActivity : AppCompatActivity() {
    val db: FirebaseFirestore = Firebase.firestore
    var mutableList1: MutableList<String> = mutableListOf("a")
    fun editCandidate(): java.util.ArrayList<String> {
        var candidate = java.util.ArrayList<String>()
        for (i in 0..mutableList1.size - 1) {
            candidate.add(mutableList1[i])
        }
        return candidate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_mini_game)
        var fbAuth = FirebaseAuth.getInstance()
        var uid = fbAuth?.uid.toString()
        val FamilyName = intent.getStringExtra("FamilyName")

        mutableList1.clear()
//        mutableList1.add("Test1")
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
//                                mutableUIDList.add(document.id.toString())
                                mutableList1.add(docName.data?.get("name").toString())

                                var adapter: ArrayAdapter<String>
                                adapter = ArrayAdapter(
                                    this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    mutableList1
                                )
                                mutableView.adapter = adapter


                            } else {
                                Log.d(ContentValues.TAG, "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }
                }
            }


        var layoutNumber = 0

        btn_pick.setOnClickListener {
            minigameRst_upload.visibility = View.VISIBLE
            text_result.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25F)
            val candidates: ArrayList<String> = editCandidate()
            val random = Random()
            var candidateNumber = 0

            Log.d("candidate",candidates.toString())

            if (candidates[0] == "" || candidates[1] == "") {
                Toast.makeText(this, "후보를 입력해주세요!", Toast.LENGTH_SHORT).show()
//                text_result.text = info
            } else {
                for (i in 0..layoutNumber + 1) {
                    if (candidates[i] != "") {
                        candidateNumber += 1
                    }
                }
                val num = random.nextInt(candidateNumber)
                text_result.text = candidates[num]
            }
            val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            text_result.startAnimation(animationFadeIn)
            minigameRst_upload.startAnimation(animationFadeIn)
        }

        btn_one_pick.setOnClickListener {
            minigameRst_upload.visibility = View.VISIBLE
            text_result.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20F)
            val candidates: ArrayList<String> = editCandidate()
            var candidateNumber = 0

            Log.d("candidate",candidates.toString())

            if (candidates[0] == "" || candidates[1] == "") {
                Toast.makeText(this, "후보를 입력해주세요!", Toast.LENGTH_SHORT).show()
//                text_result.text = info
            } else {
//                for (i in 0..layoutNumber + 1) {
//                    if (candidates[i] != "") {
//                        candidateNumber += 1
//                    }
//                }
                candidateNumber = candidates.size
                Log.d("candidateNN",candidateNumber.toString())

                var temp: String
                var temp2: String
                var randomNum1: Int
                var randomNum2: Int

                for (i in 0..layoutNumber + 1) {
                    randomNum1 = (Math.random() * candidateNumber).toInt()
                    temp = candidates[randomNum1]
                    randomNum2 = (Math.random() * candidateNumber).toInt()
                    temp2 = candidates[randomNum2]
                    candidates[randomNum1] = temp2
                    candidates[randomNum2] = temp
                }

                if(candidateNumber == 2){
                    text_result.text = "[1등] ${candidates[0]}" + "\t\t\t\t[2등] ${candidates[1]}"
                }else{
                    text_result.text = "[1등] ${candidates[0]}" + "\t\t\t\t[2등] ${candidates[1]}"+ "\t\t\t\t[3등] ${candidates[2]}"
                }

//                text_result.text = "[1등] ${candidates[0]}" + "\t\t\t\t[2등] ${candidates[1]}"+ "\t\t\t\t[3등] ${candidates[2]}"
//                for (i in 2..layoutNumber + 1) {
//                    text_result.text =
//                        text_result.text.toString() + "\t\t\t\t[${i + 1}등] ${candidates[i]}"
//                        Log.d("candidatext",text_result.text.toString())
//                }
            }

            val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            text_result.startAnimation(animationFadeIn)
            minigameRst_upload.startAnimation(animationFadeIn)
        }

        ////////// 결과 게시판에 업로드
        minigameRst_btn.setOnClickListener {
//            var intent = Intent(this, BoardActivity::class.java)
//            intent.putExtra("message",text_result.text)
//            startActivity(intent)

            var uid = fbAuth?.uid.toString() // uid
            val current = LocalDateTime.now() // 글 작성한 시간 가져오기
            val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")
            val formatted = current.format(formatter)
            val formmm1 = formatted.toString()

            var message1 = "미니게임 결과: " + text_result.text.toString()
            var location1 = ""
            var mention1 = "null"
            var PhotoBoolean = false


            val minigame_board = hashMapOf(
                // Family name
                "contents" to message1,
                "location" to location1,
                "uid" to uid,
                "time" to formmm1,
                "mention" to mention1,
                "photo" to PhotoBoolean,
            )

            db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                .document(formmm1).set(minigame_board) // 게시판 활성화
            Toast.makeText(this, "게시판 업로드 완료!!", Toast.LENGTH_SHORT).show()
        }
    }
}