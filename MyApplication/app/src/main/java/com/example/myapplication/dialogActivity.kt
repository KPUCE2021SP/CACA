package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.*
import android.Manifest
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_dialog.*
import kotlinx.android.synthetic.main.activity_home.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class dialogActivity : AppCompatActivity() {
    lateinit var btnCall : Button
//    lateinit var edtCall : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_CALL_LOG), MODE_PRIVATE
        )

        btnCall = findViewById<Button>(R.id.btnCall)
//        edtCall = findViewById<EditText>(R.id.edtCall)
        btnCall.setOnClickListener {
            edtCall.setText(findCallHistory()) /*오류*/
            setContent(dialog_contain,findCallHistory())
        }
    }

    fun findCallHistory() : String {
        var callSet = arrayOf(CallLog.Calls.DATE, CallLog.Calls.TYPE,
            CallLog.Calls.NUMBER, CallLog.Calls.DURATION)

        var c = contentResolver.query(CallLog.Calls.CONTENT_URI, /*오류*/
            callSet, null, null, null)

        if (c!!.count == 0)
            return "통화기록 없음"

        var callBuff = StringBuffer()
//        callBuff.append("\n날짜 : 구분 : 전화번호 : 통화시간\n\n")
        callBuff.append("날짜:")
        c.moveToFirst()
        do {
            var callDate = c.getLong(0)
            var datePattern = SimpleDateFormat("yyyy-MM-dd")
            var date_str = datePattern.format(Date(callDate))
            callBuff.append("$date_str:")
            if (c.getInt(1) == CallLog.Calls.INCOMING_TYPE)
                callBuff.append("착신:")
            else
                callBuff.append("발신:")
            callBuff.append(c.getString(2) + ":")
            callBuff.append(c.getString(3) + "초\n")
        } while (c.moveToNext())

        c.close()

        return callBuff.toString()


    }

    private fun setContent(layout: LinearLayout, content: String) { // 통화기록 동적생성

        if (!TextUtils.isEmpty(content)) {
            var c: String = content
//            c = c.replace("{", "")
//            c = c.replace("}", "")
            c = c.replace(" : ", ":")
            c = c.replace(" :", ":")
            c = c.replace(": ", ":")

            val splitContent = c.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val splitDate = ArrayList<String>()
            val splitSs = ArrayList<String>()
            val splitNumber = ArrayList<String>()
            val splitText = ArrayList<String>()

            // Content Text
            val mVContentView = arrayOfNulls<View>(splitContent.size)
            val mTvContentDate = arrayOfNulls<TextView>(splitContent.size)
            val mTvContentSs = arrayOfNulls<TextView>(splitContent.size)
            val mTvContentNumber = arrayOfNulls<TextView>(splitContent.size)
            val mTvContentText = arrayOfNulls<TextView>(splitContent.size)

            val mcallImageView = arrayOfNulls<ImageView>(splitContent.size)

            var token = c.split(":","\n")
            Log.d("Tk11","mmmmmmmmmmmmmmmmmmmmmmmmmmm")

            for(i in 1 until token.size){
                if (i % 4 == 0){
                    splitText.add(token[i])
                    Log.d("Tkqt", splitText.toString())
//                    Log.d("Tkqt1", splitText.size.toString())
                }else if(i % 4 == 1){
                    splitDate.add(token[i])
                    Log.d("Tkdate", splitDate.toString())
//                    Log.d("Tkdate1", splitDate.size.toString())
                }else if(i % 2 == 1){
                    splitNumber.add(token[i])
                    Log.d("Tknumber", splitNumber.toString())
//                    Log.d("Tknumber1", splitNumber.size.toString())
                }else if(i % 2 == 0){
                    splitSs.add(token[i])
                    Log.d("Tklong", splitSs.toString())
//                    Log.d("Tklong1", splitSs.size.toString())
                }
            }

            layout.removeAllViews()

            Log.d("TkCC", splitContent.size.toString())

            for (layoutIdx in splitContent.indices) {
                val layoutInflater =
                    this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val containView =
                    layoutInflater.inflate(R.layout.activity_dialog_card, null) // dialog_card를 inflate
                layout.addView(containView)

                mVContentView[layoutIdx] = containView as View

                mTvContentNumber[layoutIdx] =
                    mVContentView[layoutIdx]!!.findViewById(R.id.dialog_number) as TextView
                mTvContentSs[layoutIdx] =
                    mVContentView[layoutIdx]!!.findViewById(R.id.dialog_ss) as TextView
                mTvContentDate[layoutIdx] =
                    mVContentView[layoutIdx]!!.findViewById(R.id.dialog_date) as TextView
                mTvContentText[layoutIdx] =
                    mVContentView[layoutIdx]!!.findViewById(R.id.dialog_long) as TextView

                mTvContentDate[layoutIdx]!!.text= splitDate[layoutIdx]
                mTvContentSs[layoutIdx]!!.text = splitSs[layoutIdx]
                mTvContentNumber[layoutIdx]!!.text = splitNumber[layoutIdx]
                mTvContentText[layoutIdx]!!.text = splitText[layoutIdx]

                mcallImageView[layoutIdx] =
                    mVContentView[layoutIdx]!!.findViewById(R.id.callImageView) as ImageView  // default image 지정
                mcallImageView[layoutIdx]!!.setImageResource(R.drawable.heart)

                if (mTvContentSs[layoutIdx]!!.text.contains("착신")) { // text에 따라서 imageView 바꾸기
                    mcallImageView[layoutIdx]!!.setImageResource(R.drawable.incomingcall)
                } else if (mTvContentSs[layoutIdx]!!.text.contains("발신")) {
                    mcallImageView[layoutIdx]!!.setImageResource(R.drawable.outgoingcall)
                }
            }



//            val mlayout = arrayOfNulls<LinearLayout>(splitContent.size) // 클릭하면 삭제할 수 있도록 할 것
//            for (layoutIdx in splitContent.indices) {
//                mlayout[layoutIdx] = mVContentView[layoutIdx]!!.findViewById(R.id.allLayout) as LinearLayout  // default image 지정
//                mlayout[layoutIdx]?.setOnClickListener(){
//
//                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
//                    dlg.setTitle("항목 삭제") //제목
//                    dlg.setMessage(mTvContentNumber[layoutIdx]?.text.toString() + "를 정말 삭제하시겠습니까?") // 메시지
//                    dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
//                        // DB 삭제
//                        var fbAuth = FirebaseAuth.getInstance()
//                        val db: FirebaseFirestore = Firebase.firestore
//
//                        val docRef = db.collection("Member").document(uid)
//
//                        var a : String = mTvContentNumber[layoutIdx]?.text.toString().trim()
//                        val updates = hashMapOf<String, Any>(
//                            a to FieldValue.delete()
//                        )
//
//                        docRef.update(updates).addOnCompleteListener { }
//                    })
//                    dlg.setNegativeButton("취소", DialogInterface.OnClickListener{ dialog, which ->
//                        // 취소
//                    })
//                    dlg.show()
//
//
//
//                }
//
//
//            }

        } else {
            // TODO: get your code!
            Log.e("ERROR!", "Content is empty!");
        }


    }
}