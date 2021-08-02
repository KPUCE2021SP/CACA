package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.mypage_activity.*
import java.util.regex.Pattern

class MypageActivity : AppCompatActivity() {

    var fbAuth = FirebaseAuth.getInstance() // 로그인
    var fbFire = FirebaseFirestore.getInstance()

    var uid = fbAuth?.uid.toString() // uid
    var uemail = fbAuth?.currentUser?.email.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage_activity)

        setContent(ll_contain,DummyData.sDummyData);

        textViewName.setText(uid) // uid 확인용

        val db : FirebaseFirestore = Firebase.firestore

        val human = hashMapOf(
            "name" to "최혜민",
            "doctor" to "medicine",
            "birthday" to "20000323",
            "phone" to "01095038645",
            "address" to "school"
        )

        db.collection("Member").document(uid).set(human) // db에 넣기

    }

    private fun setContent(layout: LinearLayout, content: String) {

        if (!TextUtils.isEmpty(content)) {
            val splitContent = content.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val splitNumber = ArrayList<String>()
            val splitText = ArrayList<String>()

            // Content Text
            val mVContentView = arrayOfNulls<View>(splitContent.size)
            val mTvContentNumber = arrayOfNulls<TextView>(splitContent.size)
            val mTvContentText = arrayOfNulls<TextView>(splitContent.size)

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
                val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val containView = layoutInflater.inflate(R.layout.mypage_content, null)
                layout.addView(containView)

                mVContentView[layoutIdx] = containView as View

                mTvContentNumber[layoutIdx] = mVContentView[layoutIdx]!!.findViewById(R.id.tv_number) as TextView
                mTvContentText[layoutIdx] = mVContentView[layoutIdx]!!.findViewById(R.id.tv_text) as TextView

                mTvContentNumber[layoutIdx]!!.text = splitNumber[layoutIdx]
                mTvContentText[layoutIdx]!!.text = splitText[layoutIdx]

                if (TextUtils.isEmpty(splitNumber[layoutIdx])) {
                    mTvContentNumber[layoutIdx]!!.visibility = View.GONE
                }
            }

        } else {
            // TODO: get your code!
            Log.e("ERROR!", "Content is empty!");
        }
    }

}