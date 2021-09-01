package com.example.myapplication.FamilySet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Home_Board.HomeActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dynamic.*

class DynamicLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)

        val RandomCode = intent.getStringExtra("Random_Code")
        val FamilyName = intent.getStringExtra("FamilyName") // 제목 선정

        inviteCodeView.setText(FamilyName) // 8자리 코드 랜덤 생성해서 set


        JoinButton.setOnClickListener(){ // 참여하기 누르면 chat에 member로 추가 && Member Mypage에 가족 이름 추가

            val family = hashMapOf(
                "null" to "null"
            )

            val db: FirebaseFirestore = Firebase.firestore
            var fbAuth = FirebaseAuth.getInstance() // 로그인
            var uid = fbAuth?.uid.toString() // uid

            if (RandomCode != null) { // db에 넣기
                db.collection("Chats").document(RandomCode).collection("FamilyMember").document(uid).set(family)
//                db.collection("Member").document(uid).collection("MYPAGE").document(RandomCode).set(family)
            }


            // MypageActivity로 이동
            val intent= Intent(this, HomeActivity::class.java) // 가족 초대하기 페이지
            intent.putExtra("FamilyName", RandomCode)
            startActivity(intent)
            finish()
        }




        // 링크 타고 들어왔을 경우
        if (intent.action == Intent.ACTION_VIEW) { // 링크 타고 들어왔을 때 수행하는 코드
            val value1 = intent.data?.getQueryParameter("key") // link의 key 값을 value로 받는다.
            Toast.makeText(applicationContext, "key = $value1", Toast.LENGTH_LONG).show()
            inviteCodeView.setText(value1)
        }





//        inviteCodeImage.setOnClickListener(){ // 이미지 누르면 8자리 코드 클립보드에 복사
//            onClick_clipboard(inviteCodeView.text.toString())
//        }

        var link = "https://familyship.page.link/DynamicLinkActivity?key=" + inviteCodeView.text.toString()
        // https://familyship.page.link/DynamicLinkActivity?key=choi


        shareBtn.setOnClickListener(){ // sharebutton 누르면 링크 자체 복사
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, link) // text는 공유하고 싶은 글자

            val chooser = Intent.createChooser(intent, "공유하기")
            startActivity(chooser)
            finish()
        }


    }

    fun onClick_clipboard(texttext : String){ // 클립 보드에 복사

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("라벨", texttext)
        clipboardManager!!.setPrimaryClip(clipData)

        Toast.makeText(applicationContext, "$texttext 복사완료", Toast.LENGTH_LONG).show()
    }

}