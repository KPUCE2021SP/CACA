package com.example.myapplication.Home_Board

import android.content.ContentValues
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.SerachMap.fbAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.board.*
import kotlinx.android.synthetic.main.mypage_activity.*

class AddAccount : AppCompatActivity() {
    var uid = fbAuth?.uid.toString() // uid
    val db: FirebaseFirestore = Firebase.firestore
    lateinit var FamilyName : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        val FamilyName = intent.getStringExtra("FamilyName") // 가족 이름 가져오기



        //프로필 가져오기
        val imageName = "gs://cacafirebase-554ac.appspot.com/profiles/" + uid
        Log.d("imageName", imageName)
        val storage = Firebase.storage
        val storageRef = storage.reference
        val profileRef1 = storage.getReferenceFromUrl(imageName)
        profileRef1?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            account_profile.setImageBitmap(profilebmp)
        }?.addOnFailureListener {
            Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
        }


        // 은행 스피너
        val bankList : List<String> = listOf("은행 선택하기", "KB국민은행", "KEB 하나은행" ,"신한은행", "우리은행", "NH농협", "IBK기업은행", "카카오뱅크")
        var spinnerBank: String = ""
        var adapter: ArrayAdapter<String>
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            bankList
        )
        bank_name_spinner.adapter = adapter
        bank_name_spinner.setSelection(0, false)
        bank_name_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerBank = bankList[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val docRef1 = db.collection("Member").document(uid) // 여러 field값 가져오기
        docRef1.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    bank_profileName.setText(document.data?.get("name").toString()) // name 확인용

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }


        save_account_button.setOnClickListener {
            if(spinnerBank == "은행 선택하기"){
                Toast.makeText(applicationContext, "은행을 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (bankAccount_edt.text.toString() == ""){
                Toast.makeText(applicationContext, "계좌를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val account_content = hashMapOf(
                    "uid" to uid.toString(),
                    "bankName" to spinnerBank.toString(),
                    "bankAccount" to bankAccount_edt.text.toString(),
                )

                db.collection("Chats").document(FamilyName.toString()).collection("ACCOUNT")
                    .document(uid).set(account_content)//.set(board_content) // 게시판 활성화
                Toast.makeText(this, "계좌 등록 완료!!", Toast.LENGTH_SHORT).show()
                finish()
            }

        }
    }
}