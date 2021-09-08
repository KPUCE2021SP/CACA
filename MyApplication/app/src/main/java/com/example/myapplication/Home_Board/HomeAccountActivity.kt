package com.example.myapplication.Home_Board

import android.content.*
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
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.Mypage.MypageActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_account.view.*
import kotlinx.android.synthetic.main.activity_home_account.*

class HomeAccountActivity : AppCompatActivity() {
    val db: FirebaseFirestore = Firebase.firestore

    fun onClick_clipboard(texttext: String) { // 클립 보드에 복사

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("라벨", texttext)
        clipboardManager!!.setPrimaryClip(clipData)

        Toast.makeText(applicationContext, "$texttext 복사완료", Toast.LENGTH_LONG).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_account)
        var fbAuth = FirebaseAuth.getInstance()
        var uid = fbAuth?.uid.toString()
        val FamilyName = intent.getStringExtra("FamilyName")
        // **--------------------------------- 용돈 관리 ----------------------------------------**
        Account_Plus_Button.setOnClickListener {                //계좌 추가하기
            val intent = Intent(application, AddAccount::class.java)
            intent.putExtra("FamilyName", FamilyName)
            startActivity(intent)
        }

        //계좌 동적으로 보여주기
        var mutableAccountList: MutableList<String> = mutableListOf("a")
        mutableAccountList.clear()
        db.collection("Chats").document(FamilyName.toString()).collection("FamilyMember")
            .get()
            .addOnSuccessListener { documents ->
                for (document_acc in documents) {
                    mutableAccountList.add(document_acc.id.toString())
                    Log.d("mutableAccountList", document_acc.id.toString())
                }
                for (i in 0..(mutableAccountList.size - 1)) { // 거꾸로
                    val layoutInflater =
                        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val containView = layoutInflater.inflate(
                        R.layout.activity_account,
                        null
                    ) // mypage_content를 inflate
                    account_Layout.addView(containView)


                    val ContentView = containView as View
                    var account_name = ContentView.findViewById(R.id.account_name) as TextView // 이름
                    var account_bankName =
                        ContentView.findViewById(R.id.account_bankName) as TextView // 계좌은행
                    var account_bankNum =
                        ContentView.findViewById(R.id.account_bankNum) as TextView // 계좌 번호
                    var account_profile =
                        ContentView.findViewById(R.id.account_profile) as ImageView // profile Image
                    var account_card_Layout =
                        ContentView.findViewById(R.id.account_All) as LinearLayout
                    var account_copy_btn = ContentView.findViewById(R.id.account_copy) as ImageView
                    var account_uid: String = ""


                    // uid to Name
                    val docRef = db.collection("Member")
                        .document(mutableAccountList[(mutableAccountList.size - 1) - i])
                    docRef.get()
                        .addOnSuccessListener { document ->
                            Log.d(
                                ContentValues.TAG,
                                "DocumentSnapshot data: ${document.data}"
                            )

                            account_name.setText(document.data?.get("name").toString()) // name 확인용
                            // profile Image
                            // document2.data?.get("uid").toString()
                            val imageName =
                                "gs://cacafirebase-554ac.appspot.com/profiles/" + document.id.toString()
                            Log.d("imageName_profile", imageName)
                            val storage = Firebase.storage
                            val storageRef = storage.reference
                            val profileRef1 = storage.getReferenceFromUrl(imageName)
                            profileRef1?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                                account_profile.setImageBitmap(profilebmp) // 작성한 사람 uid로 profileImage 변경!


                            }?.addOnFailureListener {
//                                Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT)
//                                    .show()

                            }

                            if (uid == document.id) {     //삭제버튼 보이게 하기
//                                        Log.d("uiddocss", uid.toString() +":"+ document.id.toString())
//                                        rebank_btn.visibility = View.VISIBLE
                            } else {
                                Log.d("uiddocdd", uid.toString() + ":" + document.id.toString())
                                rebank_btn.visibility = View.GONE
                            }

                            account_card_Layout.entrProfile_btn?.setOnClickListener() { // 프로필 들어가기
                                val intent = Intent(application, MypageActivity::class.java)
                                Log.d("uiduiduid", document.id)
                                intent.putExtra("uid", document.id)
                                startActivity(intent)

                            }
                        }


                    val docRef1 =
                        db.collection("Chats").document(FamilyName.toString()).collection("ACCOUNT")
                            .document(mutableAccountList[(mutableAccountList.size - 1) - i]) // 여러 field값 가져오기
                    docRef1.get()
                        .addOnSuccessListener { document3 ->
                            if (document3 != null) {
                                Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document3.data}")
                                //textViewName.setText(document.data?.get("name").toString()) // name 확인용
                                account_bankName.setText(document3.data?.get("bankName").toString())
                                account_bankNum.setText(
                                    document3.data?.get("bankAccount").toString()
                                )

                                account_uid = document3.data?.get("uid").toString()

                            } else {
                                Log.d(ContentValues.TAG, "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }


                    account_card_Layout.rebank_btn?.setOnClickListener() { // 계좌 삭제

                        val dlg_account: AlertDialog.Builder = AlertDialog.Builder(
                            this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                        )
                        dlg_account.setTitle("항목 삭제") //제목
                        dlg_account.setMessage(account_name.text.toString() + "의 계좌를 정말 삭제하시겠습니까?") // 메시지
                        dlg_account.setPositiveButton(
                            "확인",
                            DialogInterface.OnClickListener { dialog, which ->
                                // DB 삭제
                                var fbAuth = FirebaseAuth.getInstance()
                                val db: FirebaseFirestore = Firebase.firestore

                                val docRef = db.collection("Chats").document(FamilyName.toString())
                                    .collection("ACCOUNT").document(account_uid.toString())
                                    .delete()

                            })
                        dlg_account.setNegativeButton(
                            "취소",
                            DialogInterface.OnClickListener { dialog, which ->
                                // 취소
                            })
                        dlg_account.show()
//
//                            return@setOnLongClickListener
                    }

                    account_copy_btn.setOnClickListener { //계좌 복사
                        var copy_account_num: String = ""
                        copy_account_num =
                            account_bankName.text.toString() + " " + account_bankNum.text.toString()

                        Log.d("bankAccount", copy_account_num.toString())
                        onClick_clipboard(copy_account_num.toString())
                    }

//                        account_card_Layout.entrProfile_btn?.setOnClickListener() { // 프로필 들어가기
//                            val intent = Intent(application, MypageActivity::class.java)
//                            intent.putExtra("uid", uid.toString())
//                            startActivity(intent)
//
//                        }

                }
            }
    }
}