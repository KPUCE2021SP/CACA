package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.mypage_activity.*
import kotlinx.android.synthetic.main.mypage_content.*
import kotlinx.android.synthetic.main.mypage_edit.*
import java.util.*
import java.util.regex.Pattern
import androidx.core.content.ContextCompat.getSystemServiceName
import androidx.core.content.ContextCompat.getSystemService
import com.google.api.Distribution
import com.google.firebase.firestore.FieldValue

class MypageActivity : AppCompatActivity() {

    var fbAuth = FirebaseAuth.getInstance() // 로그인
    var fbFire = FirebaseFirestore.getInstance()

    var uid = fbAuth?.uid.toString() // uid
    var uemail = fbAuth?.currentUser?.email.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage_activity)

        textViewName.setText(uid) // uid 확인용

        val db: FirebaseFirestore = Firebase.firestore
//
//        val human = hashMapOf(
//            "name" to "최혜민",
//            "doctor" to "medicine",
//            "birthday" to "20000323",
//            "phone" to "01095038645",
//            "address" to "school"
//        )
//
//        db.collection("Member").document(uid).set(human) // db에 넣기


        // firestore에서 정보 받아와서 DummyData 덮어쓰기

        val docRef = db.collection("Member").document(uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) { // DATA 받아왔을 때
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    //updateDateView.setText(document.data.toString()) // 받아오기 확인용
                    DummyData.sDummyData = document.data.toString()
                    //textViewName.setText(DummyData.sDummyData) // Dummydata 덮어쓰기 확인용

                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


        // DummyData로 inflate하기
        setContent(ll_contain, DummyData.sDummyData) // inflate






        mypageAddBtn.setOnClickListener { // 수정 & 추가 dialog
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.mypage_edit, null)
            val dialogText_title = dialogView.findViewById<EditText>(R.id.list_which)
            val dialogText_context = dialogView.findViewById<EditText>(R.id.mypage_edittext)

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    var fbAuth = FirebaseAuth.getInstance()
                    //firestore에 넣기
                    val db: FirebaseFirestore = Firebase.firestore
                    var uid = fbAuth?.uid.toString()
                    var map = mutableMapOf<String, Any>()
                    map[dialogText_title.text.toString()] = dialogText_context.text.toString()
                    db.collection("Member").document(uid).update(map)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(applicationContext, "업데이트 되었습니다", Toast.LENGTH_SHORT)
                                    .show()
//                                val intent = Intent(this, MypageActivity::class.java)
//                                startActivity(intent)
                            }
                        }
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */
                }
                .show()
        }




        srl_main.setOnRefreshListener { // 새로고침
            // 사용자가 아래로 드래그 했다가 놓았을 때 호출 됩니다.
            // 이때 새로고침 화살표가 계속 돌아갑니다.
            val docRef = db.collection("Member").document(uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) { // DATA 받아왔을 때
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        //updateDateView.setText(document.data.toString()) // 받아오기 확인용
                        DummyData.sDummyData = document.data.toString()
                        //textViewName.setText(DummyData.sDummyData) // Dummydata 덮어쓰기 확인용

                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
            setContent(ll_contain, DummyData.sDummyData) // inflate
            srl_main.isRefreshing = false // 인터넷 끊기
        }
    }


    private fun setContent(layout: LinearLayout, content: String) {

        if (!TextUtils.isEmpty(content)) {
            var c: String = content
            c = c.replace("{", "")
            c = c.replace("}", "")
            c = c.replace("=", " : ")

            val splitContent = c.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val splitNumber = ArrayList<String>()
            val splitText = ArrayList<String>()

            // Content Text
            val mVContentView = arrayOfNulls<View>(splitContent.size)
            val mTvContentNumber = arrayOfNulls<TextView>(splitContent.size)
            val mTvContentText = arrayOfNulls<TextView>(splitContent.size)

            val mMedicineImageView = arrayOfNulls<ImageView>(splitContent.size)



            for (splitIdx in splitContent.indices) {
                if (TextUtils.isEmpty(splitContent[splitIdx])) {
                    splitNumber.add("")
                    splitText.add("")

                } else if (Pattern.matches("^[0-9]+$", splitContent[splitIdx].substring(0, 1))) {
                    splitNumber.add(splitContent[splitIdx].substring(0, 2))
                    splitText.add(splitContent[splitIdx].substring(2).trim { it <= ' ' })

                } else {
                    splitNumber.add("")
                    splitText.add(splitContent[splitIdx])
                }
            }

            layout.removeAllViews()

            for (layoutIdx in splitContent.indices) {
                val layoutInflater =
                    this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val containView =
                    layoutInflater.inflate(R.layout.mypage_content, null) // mypage_content를 inflate
                layout.addView(containView)

                mVContentView[layoutIdx] = containView as View

                mTvContentNumber[layoutIdx] =
                    mVContentView[layoutIdx]!!.findViewById(R.id.tv_number) as TextView
                mTvContentText[layoutIdx] =
                    mVContentView[layoutIdx]!!.findViewById(R.id.tv_text) as TextView


                var str1: String = splitText[layoutIdx]
                str1 = str1.substring(0, str1.indexOf(" :")).trim()
                mTvContentNumber[layoutIdx]!!.text = str1 // 위 textView

                var str2: String = splitText[layoutIdx]
                str2 = str2.substring(str2.indexOf(": "), str2.length)
                mTvContentText[layoutIdx]!!.text = str2 // 아래 textView


                mMedicineImageView[layoutIdx] =
                    mVContentView[layoutIdx]!!.findViewById(R.id.medicineImageView) as ImageView  // default image 지정
                mMedicineImageView[layoutIdx]!!.setImageResource(R.drawable.heart)

                if (mTvContentNumber[layoutIdx]!!.text.contains("doctor")) { // text에 따라서 imageView 바꾸기
                    mMedicineImageView[layoutIdx]!!.setImageResource(R.drawable.ic_input_doctor)
                } else if (mTvContentNumber[layoutIdx]!!.text.contains("birth")) {
                    mMedicineImageView[layoutIdx]!!.setImageResource(R.drawable.cake)
                } else if (mTvContentNumber[layoutIdx]!!.text.contains("address")) {
                    mMedicineImageView[layoutIdx]!!.setImageResource(R.drawable.home)
                } else if (mTvContentNumber[layoutIdx]!!.text.contains("phone")) {
                    mMedicineImageView[layoutIdx]!!.setImageResource(R.drawable.calling)
                } else if (mTvContentNumber[layoutIdx]!!.text.contains("name")) {
                    mMedicineImageView[layoutIdx]!!.setImageResource(R.drawable.boy)
                }
            }



            val mlayout = arrayOfNulls<LinearLayout>(splitContent.size) // 클릭하면 삭제할 수 있도록 할 것
            for (layoutIdx in splitContent.indices) {
                mlayout[layoutIdx] = mVContentView[layoutIdx]!!.findViewById(R.id.allLayout) as LinearLayout  // default image 지정
                mlayout[layoutIdx]?.setOnClickListener(){

                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                    dlg.setTitle("항목 삭제") //제목
                    dlg.setMessage(mTvContentNumber[layoutIdx]?.text.toString() + "를 정말 삭제하시겠습니까?") // 메시지
                    dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        // DB 삭제
                        var fbAuth = FirebaseAuth.getInstance()
                        val db: FirebaseFirestore = Firebase.firestore

                        val docRef = db.collection("Member").document(uid)

                        var a : String = mTvContentNumber[layoutIdx]?.text.toString().trim()
                        val updates = hashMapOf<String, Any>(
                                a to FieldValue.delete()
                        )

                        docRef.update(updates).addOnCompleteListener { }
                    })
                    dlg.setNegativeButton("취소", DialogInterface.OnClickListener{ dialog, which ->
                        // 취소
                    })
                    dlg.show()



                }


//                var str1: String = splitText[layoutIdx] // 수정하는 부분은 다른 코드
//                str1 = str1.substring(0, str1.indexOf(" :"))
//                list_which.setText(str1)
//
//                var str2: String = splitText[layoutIdx]
//                str2 = str2.substring(str2.indexOf(": "), str2.length)
//                mypage_edittext.setText(str2) // 아래 textView
//
//                var list_imageView_f = findViewById<ImageView>(R.id.list_imageView)
//                if (mTvContentNumber[layoutIdx]!!.text.contains("doctor")) { // text에 따라서 imageView 바꾸기
//                    list_imageView_f.setImageResource(R.drawable.ic_input_doctor)
//                } else if (mTvContentNumber[layoutIdx]!!.text.contains("birth")) {
//                    list_imageView_f.setImageResource(R.drawable.cake)
//                } else if (mTvContentNumber[layoutIdx]!!.text.contains("address")) {
//                    list_imageView_f.setImageResource(R.drawable.home)
//                } else if (mTvContentNumber[layoutIdx]!!.text.contains("phone")) {
//                    list_imageView_f.setImageResource(R.drawable.calling)
//                } else if (mTvContentNumber[layoutIdx]!!.text.contains("name")) {
//                    list_imageView_f.setImageResource(R.drawable.boy)
//                }


            }

        } else {
            // TODO: get your code!
            Log.e("ERROR!", "Content is empty!");
        }
    }

}