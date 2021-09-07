package com.example.myapplication.Home_Board


import android.Manifest
import android.app.AlertDialog
import android.app.TabActivity
import android.content.*
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.isGone
import com.example.myapplication.FamilySet.DynamicLinkActivity
import com.example.myapplication.Mypage.MypageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.board.*
import kotlinx.android.synthetic.main.mypage_activity.*
import kotlinx.android.synthetic.main.notice_card.*
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_account.view.*
import kotlinx.android.synthetic.main.activity_custom.*
import kotlinx.android.synthetic.main.activity_dynamic.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import android.content.Intent
import android.provider.CallLog
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.CalendarKotlin.Schedule
import com.example.myapplication.CalendarKotlin.ScheduleAdapter
import com.example.myapplication.Location.AlarmReceiver.Companion.TAG
import com.example.myapplication.CalendarKotlin.ScheduleEditActivity
import com.example.myapplication.Notification.NotificationData
import com.example.myapplication.Notification.PushNotification
import com.example.myapplication.Notification.RetrofitInstance
import com.example.myapplication.R
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_board_content.*
import kotlinx.android.synthetic.main.activity_dialog.*
//import kotlinx.android.synthetic.main.activity_home.btnCall
import kotlinx.android.synthetic.main.activity_home.btnSend
import kotlinx.android.synthetic.main.activity_home.etMessage
import kotlinx.android.synthetic.main.activity_home.etTitle
import kotlinx.android.synthetic.main.activity_home.etToken

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.activity_schedule_content.*
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import kotlinx.android.synthetic.main.activity_schedule_main.*
import kotlinx.android.synthetic.main.activity_schedule_main.fab
import kotlinx.android.synthetic.main.todo_left.*
import kotlinx.android.synthetic.main.todo_right.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class HomeActivity : TabActivity() {
    var mutableList: MutableList<String> = mutableListOf("a")
    var mutableUIDList: MutableList<String> = mutableListOf("null")
    var mutableList1: MutableList<String> = mutableListOf("a")
    val db: FirebaseFirestore = Firebase.firestore

    // Calendar
//    private lateinit var realm: Realm
    private lateinit var selectedCalendar: Calendar

    // Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        realm.close()
    }

    // 끝 // Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar // 끝

    fun onClick_clipboard(texttext: String) { // 클립 보드에 복사

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("라벨", texttext)
        clipboardManager!!.setPrimaryClip(clipData)

        Toast.makeText(applicationContext, "$texttext 복사완료", Toast.LENGTH_LONG).show()
    }

//    // mmmmmmmmmmmmmmmmmmmmmmmm 안부 동적생성 mmmmmmmmmmmmmmmmmmmmmmmm //
//    fun findCallHistory() : String {
//        var callSet = arrayOf(
//            CallLog.Calls.DATE, CallLog.Calls.TYPE,
//            CallLog.Calls.NUMBER, CallLog.Calls.DURATION)
//
//        var c = contentResolver.query(
//            CallLog.Calls.CONTENT_URI, /*오류*/
//            callSet, null, null, null)
//
//        if (c!!.count == 0)
//            return "통화기록 없음"
//
//        var callBuff = StringBuffer()
////        callBuff.append("\n날짜 : 구분 : 전화번호 : 통화시간\n\n")
//        callBuff.append("날짜:")
//        c.moveToFirst()
//        do {
//            var callDate = c.getLong(0)
//            var datePattern = SimpleDateFormat("yyyy-MM-dd")
//            var date_str = datePattern.format(Date(callDate))
//            callBuff.append("$date_str:")
//            if (c.getInt(1) == CallLog.Calls.INCOMING_TYPE)
//                callBuff.append("착신:")
//            else
//                callBuff.append("발신:")
//            callBuff.append(c.getString(2) + ":")
//            callBuff.append(c.getString(3) + "초\n")
//        } while (c.moveToNext())
//
//        c.close()
//
//        return callBuff.toString()
//
//
//    }
//
//    private fun setContent(layout: LinearLayout, content: String) { // 통화기록 동적생성
//
//        if (!TextUtils.isEmpty(content)) {
//            var c: String = content
////            c = c.replace("{", "")
////            c = c.replace("}", "")
//            c = c.replace(" : ", ":")
//            c = c.replace(" :", ":")
//            c = c.replace(": ", ":")
//
//            val splitContent = c.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//
//            val splitDate = ArrayList<String>()
//            val splitSs = ArrayList<String>()
//            val splitNumber = ArrayList<String>()
//            val splitText = ArrayList<String>()
//
//            // Content Text
//            val mVContentView = arrayOfNulls<View>(splitContent.size)
//            val mTvContentDate = arrayOfNulls<TextView>(splitContent.size)
//            val mTvContentSs = arrayOfNulls<TextView>(splitContent.size)
//            val mTvContentNumber = arrayOfNulls<TextView>(splitContent.size)
//            val mTvContentText = arrayOfNulls<TextView>(splitContent.size)
//
//            val mcallImageView = arrayOfNulls<ImageView>(splitContent.size)
//
//            var token = c.split(":","\n")
//            Log.d("Tk11","mmmmmmmmmmmmmmmmmmmmmmmmmmm")
//
//            for(i in 1 until token.size){
//                if (i % 4 == 0){
//                    splitText.add(token[i])
//                    Log.d("Tkqt", splitText.toString())
////                    Log.d("Tkqt1", splitText.size.toString())
//                }else if(i % 4 == 1){
//                    splitDate.add(token[i])
//                    Log.d("Tkdate", splitDate.toString())
////                    Log.d("Tkdate1", splitDate.size.toString())
//                }else if(i % 2 == 1){
//                    splitNumber.add(token[i])
//                    Log.d("Tknumber", splitNumber.toString())
////                    Log.d("Tknumber1", splitNumber.size.toString())
//                }else if(i % 2 == 0){
//                    splitSs.add(token[i])
//                    Log.d("Tklong", splitSs.toString())
////                    Log.d("Tklong1", splitSs.size.toString())
//                }
//            }
//
//            layout.removeAllViews()
//
//            Log.d("TkCC", splitContent.size.toString())
//
//            for (layoutIdx in splitContent.indices) {
//                val layoutInflater =
//                    this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                val containView =
//                    layoutInflater.inflate(R.layout.activity_dialog_card, null) // dialog_card를 inflate
//                layout.addView(containView)
//
//                mVContentView[layoutIdx] = containView as View
//
//                mTvContentNumber[layoutIdx] =
//                    mVContentView[layoutIdx]!!.findViewById(R.id.dialog_number) as TextView
//                mTvContentSs[layoutIdx] =
//                    mVContentView[layoutIdx]!!.findViewById(R.id.dialog_ss) as TextView
//                mTvContentDate[layoutIdx] =
//                    mVContentView[layoutIdx]!!.findViewById(R.id.dialog_date) as TextView
//                mTvContentText[layoutIdx] =
//                    mVContentView[layoutIdx]!!.findViewById(R.id.dialog_long) as TextView
//
//                mTvContentDate[layoutIdx]!!.text= splitDate[layoutIdx]
//                mTvContentSs[layoutIdx]!!.text = splitSs[layoutIdx]
//                mTvContentNumber[layoutIdx]!!.text = splitNumber[layoutIdx]
//                mTvContentText[layoutIdx]!!.text = splitText[layoutIdx]
//
//                mcallImageView[layoutIdx] =
//                    mVContentView[layoutIdx]!!.findViewById(R.id.callImageView) as ImageView  // default image 지정
//                mcallImageView[layoutIdx]!!.setImageResource(R.drawable.heart)
//
//                if (mTvContentSs[layoutIdx]!!.text.contains("착신")) { // text에 따라서 imageView 바꾸기
//                    mcallImageView[layoutIdx]!!.setImageResource(R.drawable.incomingcall)
//                } else if (mTvContentSs[layoutIdx]!!.text.contains("발신")) {
//                    mcallImageView[layoutIdx]!!.setImageResource(R.drawable.outgoingcall)
//                }
//            }
//
//
//
////            val mlayout = arrayOfNulls<LinearLayout>(splitContent.size) // 클릭하면 삭제할 수 있도록 할 것
////            for (layoutIdx in splitContent.indices) {
////                mlayout[layoutIdx] = mVContentView[layoutIdx]!!.findViewById(R.id.allLayout) as LinearLayout  // default image 지정
////                mlayout[layoutIdx]?.setOnClickListener(){
////
////                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
////                    dlg.setTitle("항목 삭제") //제목
////                    dlg.setMessage(mTvContentNumber[layoutIdx]?.text.toString() + "를 정말 삭제하시겠습니까?") // 메시지
////                    dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
////                        // DB 삭제
////                        var fbAuth = FirebaseAuth.getInstance()
////                        val db: FirebaseFirestore = Firebase.firestore
////
////                        val docRef = db.collection("Member").document(uid)
////
////                        var a : String = mTvContentNumber[layoutIdx]?.text.toString().trim()
////                        val updates = hashMapOf<String, Any>(
////                            a to FieldValue.delete()
////                        )
////
////                        docRef.update(updates).addOnCompleteListener { }
////                    })
////                    dlg.setNegativeButton("취소", DialogInterface.OnClickListener{ dialog, which ->
////                        // 취소
////                    })
////                    dlg.show()
////
////
////
////                }
////
////
////            }
//
//        } else {
//            // TODO: get your code!
//            Log.e("ERROR!", "Content is empty!");
//        }
//
//
//    }

    // mmmmmmmmmmmmmmmmmmmmmmmm 미니게임 함수 mmmmmmmmmmmmmmmmmmmmmmmm //
    fun editCandidate(): ArrayList<String> {
        var candidate = ArrayList<String>()
        for (i in 0..mutableList1.size - 1) {
            candidate.add(mutableList1[i])
        }
        return candidate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val FamilyName = intent.getStringExtra("FamilyName") // 제목 선정
        FamilyNameTextView.text = FamilyName


        var fbAuth = FirebaseAuth.getInstance() // 로그인
        var fbFire = FirebaseFirestore.getInstance()
        var uid = fbAuth?.uid.toString() // uid
        val storage = Firebase.storage

        val docRef2 = db.collection("Member").document(uid).collection("MYPAGE")
            .document(FamilyName.toString())
        docRef2.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    FamilyNameTextView.text =
                        document.data?.get("name").toString() // family name 넣기

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }


        //val tabHost = this.tabHost

        val tabSpecHome = tabhost.newTabSpec("HOME").setIndicator("메인")
        tabSpecHome.setContent(R.id.tabHome)
        tabhost.addTab(tabSpecHome)

        val tabSpecBoard = tabhost.newTabSpec("BOARD").setIndicator("게시판")
        tabSpecBoard.setContent(R.id.tabBoard)
        tabhost.addTab(tabSpecBoard)

        val tabSpecCalendar = tabhost.newTabSpec("CALENDAR").setIndicator("달력")
        tabSpecCalendar.setContent(R.id.tabCalendar)

        tabhost.addTab(tabSpecCalendar)

        val tabSpecAlbum = tabhost.newTabSpec("ALBUM").setIndicator("앨범")
        tabSpecAlbum.setContent(R.id.tabAlbum)
        tabhost.addTab(tabSpecAlbum)

        tabhost.currentTab = 0

        btnMain1.setOnClickListener {
//            LinearMainpage.visibility = View.GONE
//            LinearMain1.visibility = View.VISIBLE
//            LinearMain2.visibility = View.GONE
//            LinearMain3.visibility = View.GONE
//            LinearMain4.visibility = View.GONE
//            LinearMain5.visibility = View.GONE
//            btnMain1.setTextColor(Color.WHITE)
        }
        btnMain2.setOnClickListener {
//            LinearMainpage.visibility = View.GONE
//            LinearMain1.visibility = View.GONE
//            LinearMain2.visibility = View.VISIBLE
//            LinearMain3.visibility = View.GONE
//            LinearMain4.visibility = View.GONE
//            LinearMain5.visibility = View.GONE
//            btnMain1.setTextColor(Color.WHITE)

            val intent = Intent(application, HomeDialogActivity::class.java)
            intent.putExtra("FamilyName", FamilyName)
            startActivity(intent)
        }
        btnMain3.setOnClickListener {
//
//            LinearMainpage.visibility = View.GONE
//            LinearMain1.visibility = View.GONE
//            LinearMain2.visibility = View.GONE
//            LinearMain3.visibility = View.VISIBLE
//            LinearMain4.visibility = View.GONE
//            LinearMain5.visibility = View.GONE
        }
        btnMain4.setOnClickListener {
//            LinearMainpage.visibility = View.GONE
//            LinearMain1.visibility = View.GONE
//            LinearMain2.visibility = View.GONE
//            LinearMain3.visibility = View.GONE
//            LinearMain4.visibility = View.VISIBLE
//            LinearMain5.visibility = View.GONE
//            btnMain1.setTextColor(Color.WHITE)
        }
        btnMain5.setOnClickListener {
//            LinearMainpage.visibility = View.GONE
//            LinearMain1.visibility = View.GONE
//            LinearMain2.visibility = View.GONE
//            LinearMain3.visibility = View.GONE
//            LinearMain4.visibility = View.GONE
//            LinearMain5.visibility = View.VISIBLE
//            btnMain1.setTextColor(Color.WHITE)
        }

//        /*mmmmmmmmmmmmmmmmmmmmmmmmm안부 동적 생성mmmmmmmmmmmmmmmmmmmmmmmmmm*/
//        ActivityCompat.requestPermissions(this,
//            arrayOf(Manifest.permission.READ_CALL_LOG), MODE_PRIVATE
//        )

//        btnCall.setOnClickListener {
//
//            val nextIntent = Intent(this, HomeDialogActivity::class.java)
//            nextIntent.putExtra("FamilyName", FamilyName)
//            startActivity(nextIntent)
//
//        }
        /*mmmmmmmmmmmmmmmmmmmmmmmmm안부 동적 생성mmmmmmmmmmmmmmmmmmmmmmmmmm*/

        /***************펭귄 커스텀 화면으로 가는 버튼 추가********************/
        btn_familycustom.setOnClickListener {
            val intent = Intent(application, customActivity::class.java)
            intent.putExtra("FamilyName", FamilyName)
            startActivity(intent)
        }


        val emotion = mutableListOf<String>("angry.png", "hungry.png", "sadness.png", "smile.png")

        db.collection("Chats").document(FamilyName.toString()).collection("CUSTOM")
            .document(FamilyName.toString())
            .get()
            .addOnSuccessListener { docName ->
                if (docName != null) {
                    var body_color = docName.data?.get("bodyColor").toString()
                    val bodyName =
                        "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
                    val customRef_body = storage.getReferenceFromUrl(bodyName)
                    customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                        val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                        cu_body_Iv.setImageBitmap(customRef)
                    }?.addOnFailureListener {
                     //   Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                    }

                    customImage.setOnClickListener {    //춤추기
                        var dancing_custom = docName.data?.get("dancing").toString()
                        val dancName =
                            "gs://cacafirebase-554ac.appspot.com/custom_image/emotion/" + dancing_custom
                        val customRef_dac = storage.getReferenceFromUrl(dancName)
                        customRef_dac?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                            val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                            cu_body_Iv.setImageBitmap(customRef)
                        }?.addOnFailureListener {
                       //     Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT)
                                //.show()
                        }

                        Handler(Looper.getMainLooper()).postDelayed({
                            var body_color = docName.data?.get("bodyColor").toString()
                            val bodyName =
                                "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
                            val customRef_body = storage.getReferenceFromUrl(bodyName)
                            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                                cu_body_Iv.setImageBitmap(customRef)
                            }?.addOnFailureListener {
                       //         Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT)
                                //    .show()
                            }
                        }, 3000)


                    }

                    var custom_acc = docName.data?.get("customAcc").toString()
                    val accName =
                        "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
                    val customRef_acc = storage.getReferenceFromUrl(accName)
                    customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                        val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                        cu_acc_Iv.setImageBitmap(customRef)
                    }?.addOnFailureListener {
                    //    Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                    }


                    var custom_eye = docName.data?.get("customEye").toString()
                    val eyeName =
                        "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_eye
                    val customRef_eye = storage.getReferenceFromUrl(eyeName)
                    customRef_eye?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                        val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                        cu_eye_Iv.setImageBitmap(customRef)
                    }?.addOnFailureListener {
                   //     Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }


//        // 0906 안부 가족 전화번호 가져오기 mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
//        var mutableListCall: MutableList<String> = mutableListOf("a")
//        mutableListCall.clear()
//
//        db.collection("Chats").document(FamilyName.toString()).collection("FamilyMember")
//                .get()
//                .addOnSuccessListener { documents ->
//                    for (document1 in documents) {
//                        Log.d("memberlist", "${document1.id} => ${document1.data}")
//                        mutableListCall.add(document1.id.toString())
//
//                        Log.d("calltable", mutableListCall.toString())
//                    }
//
////                mutableListTodo.reverse()
////                Log.d("mutu",mutableListTodo.toString())
//
//                    for (i in 0..(mutableListCall.size - 1)) { // 거꾸로
//                        val layoutInflater =
//                                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                        val containView = layoutInflater.inflate(
//                                R.layout.activity_callnum_card,
//                                null
//                        )
//                        callnum_contain.addView(containView)
//
//
//                        val ContentView = containView as View
//                        var callNum_name_tv =
//                                ContentView.findViewById(R.id.callNum_name) as TextView // 타이틀
//                        var callNum_number_tv =
//                                ContentView.findViewById(R.id.callNum_number) as TextView // 체크박스
//
//
//                        val docRef1 =
//                                db.collection("Member").document(mutableListCall[(mutableListCall.size - 1) - i])
//                        docRef1.get()
//                                .addOnSuccessListener { document2 ->
//                                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document2.data}")
//                                        Log.d("callmem", (document2.data?.get("phone")).toString())
//
//
//                                    callNum_name_tv.setText(document2.data?.get("name").toString())
//                                    callNum_number_tv.setText(document2.data?.get("phone").toString())
//                                }
//
//                                .addOnFailureListener { exception ->
//                                    Log.d(ContentValues.TAG, "get failed with ", exception)
//                                }
//                    }
//                }


        ///////////////////////////////////////// 공동할일 페이지 ///////////////////////////////////////////

        /*공동할일 왼손 오른손 토글버튼*/

        toggleBtnrightleft?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                srl_Todo_left.setVisibility(View.VISIBLE)
                srl_Todo_right.visibility = View.GONE
            } else {
                srl_Todo_left.setVisibility(View.GONE)
                srl_Todo_right.visibility = View.VISIBLE
            }
        }

        /*공동할일 플러스 버튼 누르면*/

//        addtodoright.setOnClickListener {
//            val intent1 = Intent(this, todo2Activity::class.java)
//            startActivity(intent1)
//        }


        addBtnTodo.setOnClickListener { // 추가 dialog
            var TodoBoolean = false
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.activity_todo2, null)
//            val dialogText_title = dialogView.findViewById<EditText>(R.id.list_which)
            val Todo_title = dialogView.findViewById<EditText>(R.id.todoEdit)


            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    var fbAuth = FirebaseAuth.getInstance()
                    //firestore에 넣기
                    val db: FirebaseFirestore = Firebase.firestore
                    val TodoContent = hashMapOf(
                        "title" to Todo_title.text.toString(),
                        "check" to TodoBoolean,
                    )

                    db.collection("Chats").document(FamilyName.toString())
                        .collection("TODO").document(Todo_title.text.toString()).set(TodoContent)//add(TodoContent)
//                        .document("title").set(TodoContent)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(applicationContext, "할일 추가 완료", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */
                }
                .show()
        }

        ////////////////////// 공동할일 받아오기 왼/오 ////////////////////////////
        var mutableListTodo: MutableList<String> = mutableListOf("a")
        mutableListTodo.clear()

        db.collection("Chats").document(FamilyName.toString())
            .collection("TODO")
            .get()
            .addOnSuccessListener { documents ->
                for (document1 in documents) {
                    Log.d("TodoList", "${document1.id} => ${document1.data}")
                    mutableListTodo.add(document1.id.toString())
                }

//                mutableListTodo.reverse()
//                Log.d("mutu",mutableListTodo.toString())

                for (i in 0..(mutableListTodo.size - 1)) { // 거꾸로
                    val layoutInflater =
                        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val containView = layoutInflater.inflate(
                        R.layout.todo_right,
                        null
                    )
                    righthandList.addView(containView)


                    val ContentView = containView as View
                    var todo_right_tv =
                        ContentView.findViewById(R.id.todo_right_tv) as TextView // 타이틀
                    var todo_right_checkbx =
                        ContentView.findViewById(R.id.todo_right_checkbx) as CheckBox // 체크박스


                    val docRef1 =
                        db.collection("Chats").document(FamilyName.toString()).collection("TODO")
                            .document(mutableListTodo[(mutableListTodo.size - 1) - i]) // 여러 field값 가져오기
                    docRef1.get()
                        .addOnSuccessListener { document2 ->
                            if (document2 != null) {
                                Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document2.data}")
                                todo_right_tv.text = document2.data?.get("title").toString()
                                todo_right_checkbx.isChecked = (document2.data?.get("check") as Boolean)

//                                    todo_right_checkbx.text = (document2.data?.get("check").toString())

                                Log.d("Blean1",(document2.data?.get("check") as Boolean).toString())
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }

                    todo_right_checkbx.setOnCheckedChangeListener { buttonView, isChecked ->
                        var TodoBoolean: Boolean = isChecked
//                        todo_right_checkbx.text = isChecked.toString()
                        val todo_update = hashMapOf(
                            "title" to todo_right_tv.text.toString(),
                            "check" to TodoBoolean,
                        )

                        Log.d("Blean", TodoBoolean.toString())
                        Log.d("Blean2", todo_right_tv.text.toString())
                        db.collection("Chats").document(FamilyName.toString()).collection("TODO")
                            .document(todo_right_tv.text.toString()).update(todo_update as Map<String, Any>)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(applicationContext, "업데이트 완료!", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }

                    righthandcard?.setOnClickListener() { // 삭제
                        val dlg: AlertDialog.Builder = AlertDialog.Builder(
                            this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                        )
                        dlg.setTitle("항목 삭제") //제목
                        dlg.setMessage(todo_right_tv.text.toString() + "를 정말 삭제하시겠습니까?") // 메시지
                        dlg.setPositiveButton(
                            "확인",
                            DialogInterface.OnClickListener { dialog, which ->
                                // DB 삭제
                                var fbAuth = FirebaseAuth.getInstance()
                                val db: FirebaseFirestore = Firebase.firestore

                                val docRef = db.collection("Chats").document(FamilyName.toString())
                                    .collection("TODO").document(todo_right_tv.text.toString())
                                    .delete()
                            })
                        dlg.setNegativeButton(
                            "취소",
                            DialogInterface.OnClickListener { dialog, which ->
                                // 취소
                            })
                        dlg.show()
                    }
                }



                ///////////////////////////////////// 왼손 받아오기
                for (i in 0..(mutableListTodo.size - 1)) { // 거꾸로
                    val layoutInflaterL =
                        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val containViewL = layoutInflaterL.inflate(
                        R.layout.todo_left,
                        null
                    )
                    lefthandList.addView(containViewL)


                    val ContentViewL = containViewL as View
                    var todo_left_tv =
                        ContentViewL.findViewById(R.id.todo_left_tv) as TextView // 타이틀
                    var todo_left_checkbx =
                        ContentViewL.findViewById(R.id.todo_left_checkbx) as CheckBox // 체크박스


                    val docRef2 =
                        db.collection("Chats").document(FamilyName.toString()).collection("TODO")
                            .document(mutableListTodo[(mutableListTodo.size - 1) - i]) // 여러 field값 가져오기
                    docRef2.get()
                        .addOnSuccessListener { document2 ->
                            if (document2 != null) {
                                Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document2.data}")
                                todo_left_tv.text = document2.data?.get("title").toString()
                                todo_left_checkbx.isChecked = (document2.data?.get("check") as Boolean)

//                                    todo_right_checkbx.text = (document2.data?.get("check").toString())

                                Log.d("Blean1",(document2.data?.get("check") as Boolean).toString())
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }

                    todo_left_checkbx.setOnCheckedChangeListener { buttonView, isChecked ->
                        var TodoBoolean: Boolean = isChecked
//                        todo_right_checkbx.text = isChecked.toString()
                        val todo_update = hashMapOf(
                            "title" to todo_left_tv.text.toString(),
                            "check" to TodoBoolean,
                        )

                        Log.d("Blean", TodoBoolean.toString())
                        Log.d("Blean2", todo_left_tv.text.toString())
                        db.collection("Chats").document(FamilyName.toString()).collection("TODO")
                            .document(todo_left_tv.text.toString()).update(todo_update as Map<String, Any>)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(applicationContext, "업데이트 완료!", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }

                    righthandcard?.setOnClickListener() { // 삭제
                        val dlg: AlertDialog.Builder = AlertDialog.Builder(
                            this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                        )
                        dlg.setTitle("항목 삭제") //제목
                        dlg.setMessage(todo_right_tv.text.toString() + "를 정말 삭제하시겠습니까?") // 메시지
                        dlg.setPositiveButton(
                            "확인",
                            DialogInterface.OnClickListener { dialog, which ->
                                // DB 삭제
                                var fbAuth = FirebaseAuth.getInstance()
                                val db: FirebaseFirestore = Firebase.firestore

                                val docRef = db.collection("Chats").document(FamilyName.toString())
                                    .collection("TODO").document(todo_right_tv.text.toString())
                                    .delete()
                            })
                        dlg.setNegativeButton(
                            "취소",
                            DialogInterface.OnClickListener { dialog, which ->
                                // 취소
                            })
                        dlg.show()
                    }
                    //
                    lefthandcard?.setOnClickListener() { // 삭제
                        val dlg: AlertDialog.Builder = AlertDialog.Builder(
                            this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                        )
                        dlg.setTitle("항목 삭제") //제목
                        dlg.setMessage(todo_left_tv.text.toString() + "를 정말 삭제하시겠습니까?") // 메시지
                        dlg.setPositiveButton(
                            "확인",
                            DialogInterface.OnClickListener { dialog, which ->
                                // DB 삭제
                                var fbAuth = FirebaseAuth.getInstance()
                                val db: FirebaseFirestore = Firebase.firestore

                                val docRef = db.collection("Chats").document(FamilyName.toString())
                                    .collection("TODO").document(todo_left_tv.text.toString())
                                    .delete()
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

/////////////////////////////////////////// 공동할일 새로고침 /////////////////////////////////////////////////////////////////////////
        srl_Todo_right.setOnRefreshListener {
            // 게시판 동적 생성
            // Board_LineaLayout
            righthandList.removeAllViews()
            var mutableListTodo: MutableList<String> = mutableListOf("a")
            mutableListTodo.clear()

            db.collection("Chats").document(FamilyName.toString())
                .collection("TODO")
                .get()
                .addOnSuccessListener { documents ->
                    for (document1 in documents) {
                        Log.d("TodoList", "${document1.id} => ${document1.data}")
                        mutableListTodo.add(document1.id.toString())
                    }

//                mutableListTodo.reverse()
//                Log.d("mutu",mutableListTodo.toString())

                    for (i in 0..(mutableListTodo.size - 1)) { // 거꾸로
                        val layoutInflater =
                            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val containView = layoutInflater.inflate(
                            R.layout.todo_right,
                            null
                        )
                        righthandList.addView(containView)


                        val ContentView = containView as View
                        var todo_right_tv =
                            ContentView.findViewById(R.id.todo_right_tv) as TextView // 타이틀
                        var todo_right_checkbx =
                            ContentView.findViewById(R.id.todo_right_checkbx) as CheckBox // 체크박스


                        val docRef1 =
                            db.collection("Chats").document(FamilyName.toString()).collection("TODO")
                                .document(mutableListTodo[(mutableListTodo.size - 1) - i]) // 여러 field값 가져오기
                        docRef1.get()
                            .addOnSuccessListener { document2 ->
                                if (document2 != null) {
                                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document2.data}")
                                    todo_right_tv.text = document2.data?.get("title").toString()
                                    todo_right_checkbx.isChecked = (document2.data?.get("check") as Boolean)

//                                    todo_right_checkbx.text = (document2.data?.get("check").toString())

                                    Log.d("Blean1",(document2.data?.get("check") as Boolean).toString())
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(ContentValues.TAG, "get failed with ", exception)
                            }

                        todo_right_checkbx.setOnCheckedChangeListener { buttonView, isChecked ->
                            var TodoBoolean: Boolean = isChecked
//                                todo_right_checkbx.text = isChecked.toString()
                            val todo_update = hashMapOf(
                                "title" to todo_right_tv.text.toString(),
                                "check" to TodoBoolean,
                            )

                            Log.d("Blean", TodoBoolean.toString())
                            Log.d("Blean2", todo_right_tv.text.toString())
                            db.collection("Chats").document(FamilyName.toString()).collection("TODO")
                                .document(todo_right_tv.text.toString()).update(todo_update as Map<String, Any>)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(applicationContext, "업데이트 완료!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        }

                        righthandcard?.setOnClickListener() { // 삭제

                            val dlg: AlertDialog.Builder = AlertDialog.Builder(
                                this,
                                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                            )
                            dlg.setTitle("항목 삭제") //제목
                            dlg.setMessage(todo_right_tv.text.toString() + "를 정말 삭제하시겠습니까?") // 메시지
                            dlg.setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialog, which ->
                                    // DB 삭제
                                    var fbAuth = FirebaseAuth.getInstance()
                                    val db: FirebaseFirestore = Firebase.firestore

                                    val docRef = db.collection("Chats").document(FamilyName.toString())
                                        .collection("TODO").document(todo_right_tv.text.toString())
                                        .delete()
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
            srl_Todo_right.isRefreshing = false // 인터넷 끊기
        }   // 오른손 새로고침

        srl_Todo_left.setOnRefreshListener {
            // 게시판 동적 생성
            // Board_LineaLayout
            lefthandList.removeAllViews()
            var mutableListTodo: MutableList<String> = mutableListOf("a")
            mutableListTodo.clear()

            db.collection("Chats").document(FamilyName.toString())
                .collection("TODO")
                .get()
                .addOnSuccessListener { documents ->
                    for (document1 in documents) {
                        Log.d("TodoList", "${document1.id} => ${document1.data}")
                        mutableListTodo.add(document1.id.toString())
                    }

//                mutableListTodo.reverse()
//                Log.d("mutu",mutableListTodo.toString())

                    for (i in 0..(mutableListTodo.size - 1)) { // 거꾸로
                        val layoutInflaterL =
                            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val containViewL = layoutInflaterL.inflate(
                            R.layout.todo_left,
                            null
                        )
                        lefthandList.addView(containViewL)


                        val ContentViewL = containViewL as View
                        var todo_left_tv =
                            ContentViewL.findViewById(R.id.todo_left_tv) as TextView // 타이틀
                        var todo_left_checkbx =
                            ContentViewL.findViewById(R.id.todo_left_checkbx) as CheckBox // 체크박스


                        val docRef2 =
                            db.collection("Chats").document(FamilyName.toString()).collection("TODO")
                                .document(mutableListTodo[(mutableListTodo.size - 1) - i]) // 여러 field값 가져오기
                        docRef2.get()
                            .addOnSuccessListener { document2 ->
                                if (document2 != null) {
                                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document2.data}")
                                    todo_left_tv.text = document2.data?.get("title").toString()
                                    todo_left_checkbx.isChecked = (document2.data?.get("check") as Boolean)

//                                    todo_right_checkbx.text = (document2.data?.get("check").toString())

                                    Log.d("Blean1",(document2.data?.get("check") as Boolean).toString())
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(ContentValues.TAG, "get failed with ", exception)
                            }

                        todo_left_checkbx.setOnCheckedChangeListener { buttonView, isChecked ->
                            var TodoBoolean: Boolean = isChecked
//                        todo_right_checkbx.text = isChecked.toString()
                            val todo_update = hashMapOf(
                                "title" to todo_left_tv.text.toString(),
                                "check" to TodoBoolean,
                            )

                            Log.d("Blean", TodoBoolean.toString())
                            Log.d("Blean2", todo_left_tv.text.toString())
                            db.collection("Chats").document(FamilyName.toString()).collection("TODO")
                                .document(todo_left_tv.text.toString()).update(todo_update as Map<String, Any>)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(applicationContext, "업데이트 완료!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        }
                        lefthandcard?.setOnClickListener() { // 삭제
                            val dlg: AlertDialog.Builder = AlertDialog.Builder(
                                this,
                                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                            )
                            dlg.setTitle("항목 삭제") //제목
                            dlg.setMessage(todo_left_tv.text.toString() + "를 정말 삭제하시겠습니까?") // 메시지
                            dlg.setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialog, which ->
                                    // DB 삭제
                                    var fbAuth = FirebaseAuth.getInstance()
                                    val db: FirebaseFirestore = Firebase.firestore

                                    val docRef = db.collection("Chats").document(FamilyName.toString())
                                        .collection("TODO").document(todo_left_tv.text.toString())
                                        .delete()
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
            srl_Todo_left.isRefreshing = false // 인터넷 끊기
        }    // 왼손 새로고침


        //////////////////////////////////////////////////////////////////////////////////////
        btnMyPage.setOnClickListener {
            val intent = Intent(application, MypageActivity::class.java)
            startActivity(intent)
        }

        btnGroup.setOnClickListener {
            val intent = Intent(application, DynamicLinkActivity::class.java)
            intent.putExtra("FamilyName", FamilyName)
            startActivity(intent)
        }

        Board_Plus_Button.setOnClickListener() { // 게시판 글 작성하기 페이지로 이동
            // board Format
            val current = LocalDateTime.now() // 글 작성한 시간 가져오기
            val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")
            val formatted = current.format(formatter)
//            var PhotoBoolean : Boolean = false // 사진 사용 여부
            val board_format = hashMapOf(
                // Family name
                "location" to ""
            )
            db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                .document(formatted)
                .set(board_format as Map<String, Any>)//.set(board_content) // 게시판 활성화


            val intent = Intent(application, BoardActivity::class.java)
            intent.putExtra("FamilyName", FamilyName)
            intent.putExtra("formatted", formatted)
            startActivity(intent)

        }


// Board_LineaLayout
        var mutableList: MutableList<String> = mutableListOf("a")
        mutableList.clear()

        db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
            .get()
            .addOnSuccessListener { documents ->
                for (document1 in documents) {
                    Log.d(ContentValues.TAG, "${document1.id} => ${document1.data}")
                    mutableList.add(document1.id.toString())
                }


                for (i in 0..(mutableList.size - 1)) { // 거꾸로
                    val layoutInflater =
                        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val containView = layoutInflater.inflate(
                        R.layout.notice_card,
                        null
                    ) // mypage_content를 inflate
                    Board_LinearLayout.addView(containView)

                    notice_image.background =
                        getResources().getDrawable(R.drawable.imageview_cornerround, null)
                    notice_image.setClipToOutline(true)

                    val ContentView = containView as View
                    var notice_board = ContentView.findViewById(R.id.notice_board) as TextView // 내용
                    var notice_time = ContentView.findViewById(R.id.notice_time) as TextView // 시간
                    var notice_name = ContentView.findViewById(R.id.notice_name) as TextView // uid
                    var notice_comment = ContentView.findViewById(R.id.notice_comment) as TextView // uid
                    var notice_profile =
                        ContentView.findViewById(R.id.notice_profile) as ImageView // profile Image
                    var notice_image =
                        ContentView.findViewById(R.id.notice_image) as ImageView // Board Image
                    var notice_card_Layout =
                        ContentView.findViewById(R.id.notice_card_Layout) as LinearLayout
                    var notice_delete_button = ContentView.findViewById(R.id.notice_card_delete) as ImageView // 삭제버튼



                    // 댓글 갯수
                    var mutableCommentList: MutableList<String> = mutableListOf("a")
                    mutableCommentList.clear()
                    db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                        .document(mutableList[(mutableList.size - 1) - i]).collection(mutableList[(mutableList.size - 1) - i])
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document1 in documents) {
                                mutableCommentList.add(document1.id.toString())

                            }
                            notice_comment.setText(mutableCommentList.size.toString())

                        }






                    val docRef1 =
                        db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                            .document(mutableList[(mutableList.size - 1) - i]) // 여러 field값 가져오기
                    docRef1.get()
                        .addOnSuccessListener { document2 ->
                            if (document2 != null) {
                                Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document2.data}")
                                //textViewName.setText(document.data?.get("name").toString()) // name 확인용
                                notice_time.setText(document2.data?.get("time").toString())
                                notice_board.setText(document2.data?.get("contents").toString())





                                // profile Image
                                // document2.data?.get("uid").toString()
                                val imageName =
                                    "gs://cacafirebase-554ac.appspot.com/profiles/" + document2.data?.get(
                                        "uid"
                                    ).toString()
                                Log.d("imageName", imageName)
                                val storage = Firebase.storage
                                val storageRef = storage.reference
                                val profileRef1 = storage.getReferenceFromUrl(imageName)
                                profileRef1?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                    val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                                    notice_profile.setImageBitmap(profilebmp) // 작성한 사람 uid로 profileImage 변경!
                                }?.addOnFailureListener {
//                                    Toast.makeText(
//                                        this,
//                                        "image downloade failed",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
                                }


                                // Board Image
                                val BoardImageName =
                                    "gs://cacafirebase-554ac.appspot.com/Family_Board/" + FamilyName + "_" + document2.data?.get(
                                        "time"
                                    ).toString()
                                Log.d("imageName", BoardImageName)
                                val storage2 = Firebase.storage
                                val storageRef2 = storage.reference
                                val profileRef2 = storage.getReferenceFromUrl(BoardImageName)
                                profileRef2?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                    val profilebmp2 = BitmapFactory.decodeByteArray(it, 0, it.size)
                                    notice_image.setImageBitmap(profilebmp2) // 작성한 사람 uid로 profileImage 변경!
                                }?.addOnFailureListener {
//                                    Toast.makeText(
//                                        this,
//                                        "image downloade failed",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
                                    notice_image.isGone =
                                        true                            // 업로드된 이미지가 없다면
                                }


                                // uid to Name
                                val docRef = db.collection("Member")
                                    .document(document2.data?.get("uid").toString())
                                docRef.get()
                                    .addOnSuccessListener { document3 ->
                                        if (document3 != null) {
                                            Log.d(
                                                ContentValues.TAG,
                                                "DocumentSnapshot data: ${document3.data}"
                                            )
                                            notice_name.setText(
                                                document3.data?.get("name").toString()
                                            ) // name 확인용

                                        } else {
                                            Log.d(ContentValues.TAG, "No such document")
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d(ContentValues.TAG, "get failed with ", exception)
                                    }
                                /////////////////////////// 게시글 크게보기 intent
                                notice_image.setOnClickListener {
                                    var formattedB: String = notice_time.text.toString()
                                    val secondintent =
                                        Intent(this, BoardContentActivity::class.java)

                                    secondintent.putExtra(
                                        "notice_boardB",
                                        notice_board.text.toString()
                                    )
                                    secondintent.putExtra(
                                        "notice_nameB",
                                        notice_name.text.toString()
                                    )
                                    secondintent.putExtra("notice_profileB", imageName)
                                    secondintent.putExtra("notice_imageB", BoardImageName)


                                    secondintent.putExtra("FamilyNameB", FamilyName)
                                    secondintent.putExtra("formattedB", formattedB)
                                    Log.d("Fmt1", imageName)
                                    Log.d("Fmt2", BoardImageName)
                                    startActivity(secondintent)
                                }
                                notice_board.setOnClickListener {
                                    var formattedB: String = notice_time.text.toString()
                                    val secondintent =
                                            Intent(this, BoardContentActivity::class.java)

                                    secondintent.putExtra(
                                            "notice_boardB",
                                            notice_board.text.toString()
                                    )
                                    secondintent.putExtra(
                                            "notice_nameB",
                                            notice_name.text.toString()
                                    )
                                    secondintent.putExtra("notice_profileB", imageName)
                                    secondintent.putExtra("notice_imageB", BoardImageName)


                                    secondintent.putExtra("FamilyNameB", FamilyName)
                                    secondintent.putExtra("formattedB", formattedB)
                                    Log.d("Fmt1", imageName)
                                    Log.d("Fmt2", BoardImageName)
                                    startActivity(secondintent)
                                }



                            } else {
                                Log.d(ContentValues.TAG, "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }




                    notice_delete_button?.setOnClickListener() { // 삭제

                        val dlg: AlertDialog.Builder = AlertDialog.Builder(
                            this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                        )
                        dlg.setTitle("항목 삭제") //제목
                        dlg.setMessage(notice_time.text.toString() + "를 정말 삭제하시겠습니까?") // 메시지
                        dlg.setPositiveButton(
                            "확인",
                            DialogInterface.OnClickListener { dialog, which ->
                                // DB 삭제
                                var fbAuth = FirebaseAuth.getInstance()
                                val db: FirebaseFirestore = Firebase.firestore

                                val docRef = db.collection("Chats").document(FamilyName.toString())
                                    .collection("BOARD").document(notice_time.text.toString())
                                    .delete()

                            })
                        dlg.setNegativeButton(
                            "취소",
                            DialogInterface.OnClickListener { dialog, which ->
                                // 취소
                            })
                        dlg.show()
                    }
                }
                notice_image.background =
                    getResources().getDrawable(R.drawable.imageview_cornerround, null)
                notice_image.setClipToOutline(true)
            }




        //게시판 새로고침하기
        srl_Mainpage.setOnRefreshListener {
            // 게시판 동적 생성
            // Board_LineaLayout
            Board_LinearLayout.removeAllViews()
            var mutableList: MutableList<String> = mutableListOf("a")
            mutableList.clear()

            db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                .get()
                .addOnSuccessListener { documents ->
                    for (document1 in documents) {
                        Log.d(ContentValues.TAG, "${document1.id} => ${document1.data}")
                        mutableList.add(document1.id.toString())
                    }


                    for (i in 0..(mutableList.size - 1)) { // 거꾸로
                        val layoutInflater =
                            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val containView = layoutInflater.inflate(
                            R.layout.notice_card,
                            null
                        ) // mypage_content를 inflate
                        Board_LinearLayout.addView(containView)

                        val ContentView = containView as View
                        var notice_board =
                            ContentView.findViewById(R.id.notice_board) as TextView // 내용
                        var notice_time =
                            ContentView.findViewById(R.id.notice_time) as TextView // 시간
                        var notice_name =
                            ContentView.findViewById(R.id.notice_name) as TextView // uid
                        var notice_profile =
                            ContentView.findViewById(R.id.notice_profile) as ImageView // profile Image
                        var notice_image =
                            ContentView.findViewById(R.id.notice_image) as ImageView // Board Image
                        var notice_card_Layout =
                            ContentView.findViewById(R.id.notice_card_Layout) as LinearLayout

                        var notice_delete_button = ContentView.findViewById(R.id.notice_card_delete) as ImageView // 삭제버튼

                        val docRef1 = db.collection("Chats").document(FamilyName.toString())
                            .collection("BOARD")
                            .document(mutableList[(mutableList.size - 1) - i]) // 여러 field값 가져오기
                        docRef1.get()
                            .addOnSuccessListener { document2 ->
                                if (document2 != null) {
                                    Log.d(
                                        ContentValues.TAG,
                                        "DocumentSnapshot data: ${document2.data}"
                                    )
                                    //textViewName.setText(document.data?.get("name").toString()) // name 확인용
                                    notice_time.setText(document2.data?.get("time").toString())
                                    notice_board.setText(document2.data?.get("contents").toString())
                                    //notice_board.setText(mutableList.toString()) //////////////////////////////test


                                    // profile Image
                                    // document2.data?.get("uid").toString()
                                    val imageName =
                                        "gs://cacafirebase-554ac.appspot.com/profiles/" + document2.data?.get(
                                            "uid"
                                        ).toString()
                                    Log.d("imageName", imageName)
                                    val storage = Firebase.storage
                                    val storageRef = storage.reference
                                    val profileRef1 = storage.getReferenceFromUrl(imageName)
                                    profileRef1?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                        val profilebmp =
                                            BitmapFactory.decodeByteArray(it, 0, it.size)
                                        notice_profile.setImageBitmap(profilebmp) // 작성한 사람 uid로 profileImage 변경!
                                    }?.addOnFailureListener {
//                                        Toast.makeText(
//                                            this,
//                                            "image downloade failed",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
                                    }


                                    // Board Image
                                    val BoardImageName =
                                        "gs://cacafirebase-554ac.appspot.com/Family_Board/" + FamilyName + "_" + document2.data?.get(
                                            "time"
                                        ).toString()
                                    Log.d("imageName", BoardImageName)
                                    val storage2 = Firebase.storage
                                    val storageRef2 = storage.reference
                                    val profileRef2 = storage.getReferenceFromUrl(BoardImageName)
                                    profileRef2?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                        val profilebmp2 =
                                            BitmapFactory.decodeByteArray(it, 0, it.size)
                                        notice_image.setImageBitmap(profilebmp2) // 작성한 사람 uid로 profileImage 변경!
                                    }?.addOnFailureListener {
//                                        Toast.makeText(
//                                            this,
//                                            "image downloade failed",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
                                        notice_image.isGone =
                                            true                            // 업로드된 이미지가 없다면
                                    }


                                    // uid to Name
                                    val docRef = db.collection("Member")
                                        .document(document2.data?.get("uid").toString())
                                    docRef.get()
                                        .addOnSuccessListener { document3 ->
                                            if (document3 != null) {
                                                Log.d(
                                                    ContentValues.TAG,
                                                    "DocumentSnapshot data: ${document3.data}"
                                                )
                                                notice_name.setText(
                                                    document3.data?.get("name").toString()
                                                ) // name 확인용

                                            } else {
                                                Log.d(ContentValues.TAG, "No such document")
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.d(ContentValues.TAG, "get failed with ", exception)
                                        }

                                    /////////////////////////// 게시글 크게보기 intent
                                    notice_image.setOnClickListener {
                                        var formattedB: String = notice_time.text.toString()
                                        val secondintent =
                                                Intent(this, BoardContentActivity::class.java)

                                        secondintent.putExtra(
                                                "notice_boardB",
                                                notice_board.text.toString()
                                        )
                                        secondintent.putExtra(
                                                "notice_nameB",
                                                notice_name.text.toString()
                                        )
                                        secondintent.putExtra("notice_profileB", imageName)
                                        secondintent.putExtra("notice_imageB", BoardImageName)


                                        secondintent.putExtra("FamilyNameB", FamilyName)
                                        secondintent.putExtra("formattedB", formattedB)
                                        Log.d("Fmt1", imageName)
                                        Log.d("Fmt2", BoardImageName)
                                        startActivity(secondintent)
                                    }

                                    notice_board.setOnClickListener {
                                        var formattedB: String = notice_time.text.toString()
                                        val secondintent =
                                                Intent(this, BoardContentActivity::class.java)

                                        secondintent.putExtra(
                                                "notice_boardB",
                                                notice_board.text.toString()
                                        )
                                        secondintent.putExtra(
                                                "notice_nameB",
                                                notice_name.text.toString()
                                        )
                                        secondintent.putExtra("notice_profileB", imageName)
                                        secondintent.putExtra("notice_imageB", BoardImageName)


                                        secondintent.putExtra("FamilyNameB", FamilyName)
                                        secondintent.putExtra("formattedB", formattedB)
                                        Log.d("Fmt1", imageName)
                                        Log.d("Fmt2", BoardImageName)
                                        startActivity(secondintent)
                                    }

                                } else {
                                    Log.d(ContentValues.TAG, "No such document")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(ContentValues.TAG, "get failed with ", exception)
                            }
                        notice_delete_button?.setOnClickListener() { // 삭제

                            val dlg: AlertDialog.Builder = AlertDialog.Builder(
                                this,
                                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                            )
                            dlg.setTitle("항목 삭제") //제목
                            dlg.setMessage(notice_time.text.toString() + "를 정말 삭제하시겠습니까?") // 메시지
                            dlg.setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialog, which ->
                                    // DB 삭제
                                    var fbAuth = FirebaseAuth.getInstance()
                                    val db: FirebaseFirestore = Firebase.firestore

                                    val docRef =
                                        db.collection("Chats").document(FamilyName.toString())
                                            .collection("BOARD")
                                            .document(notice_time.text.toString())
                                            .delete()

                                })
                            dlg.setNegativeButton(
                                "취소",
                                DialogInterface.OnClickListener { dialog, which ->
                                    // 취소
                                })
                            dlg.show()
                        }
                    }
                    notice_image.background =
                        getResources().getDrawable(R.drawable.imageview_cornerround, null)
                    notice_image.setClipToOutline(true)
                }

            srl_Mainpage.isRefreshing = false // 인터넷 끊기
        }


        // random pick game --------------------------------- minigame

        mutableList1.clear()
        mutableList1.add("Test1")
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

            if (candidates[0] == "" || candidates[1] == "") {
                Toast.makeText(this, "후보를 입력해주세요!", Toast.LENGTH_SHORT).show()
//                text_result.text = info
            } else {
                for (i in 0..layoutNumber + 1) {
                    if (candidates[i] != "") {
                        candidateNumber += 1
                    }
                }

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

                text_result.text = "[1등] ${candidates[0]}" + "\t\t\t\t[2등] ${candidates[1]}"
                for (i in 2..layoutNumber + 1) {
                    text_result.text =
                        text_result.text.toString() + "\t\t\t\t[${i + 1}등] ${candidates[i]}"
                }
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

// Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger


                var mutableListMessage: MutableList<String> = mutableListOf("a")
                var mutableListMessageDEVICE: MutableList<String> = mutableListOf("a")
                mutableListMessage.clear()
                mutableListMessageDEVICE.clear()

                db.collection("Chats").document(FamilyName.toString()).collection("FamilyMember")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            Log.d("FamilyMember", "${document.id}")
//                            mutableListMessageUID.add(document.id)
                            // DEVICE
                            val docRef3 = db.collection("Member").document(document.id).collection("DEVICE").document("TOKEN")
                            docRef3.get()
                                .addOnSuccessListener { docName ->
                                    if (docName != null) {
                                        mutableListMessageDEVICE.add(docName.data?.get("deviceInfo").toString())
                                        Log.d("device", mutableListMessageDEVICE.toString())
                                    } else {
                                        Log.d(ContentValues.TAG, "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(ContentValues.TAG, "get failed with ", exception)
                                }


//                    //Member 이름 가져와서 mutableNameList에 저장
                            val docRef2 = db.collection("Member").document(document.id)
                            docRef2.get()
                                .addOnSuccessListener { docName ->
                                    if (docName != null) {
                                        Log.d(
                                            ContentValues.TAG,
                                            "DocumentSnapshot data: ${docName.data}"
                                        )
                                        mutableListMessage.add(docName.data?.get("name").toString())

                                        var adapter: ArrayAdapter<String> = ArrayAdapter(
                                            this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            mutableListMessage
                                        )
                                        spinner_message.adapter = adapter

                                    } else {
                                        Log.d(ContentValues.TAG, "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(ContentValues.TAG, "get failed with ", exception)
                                }
                        }

                    }


                var spinnerUID: String = ""
                spinner_message.setSelection(0, false)
                spinner_message.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        spinnerUID = mutableListMessageDEVICE[position].toString()
                        etToken.setText(mutableListMessageDEVICE[position])
                        Log.d("spinnerUID", spinnerUID)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }

                btnSend.setOnClickListener {
                    val title = etTitle.text.toString()
                    val message = etMessage.text.toString()
                    val recipientToken = etToken.text.toString()
                    if(title != "" && message != "" && recipientToken != "") {
                        PushNotification(
                            NotificationData(title, message),
                            recipientToken
                        )
                        sendNotification(PushNotification(NotificationData(title, message),recipientToken))

                    }
                }




// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar

                val thisDate = Date()
                thisDate.hours = 0
                thisDate.minutes = 0
                thisDate.seconds = 0
                selectedCalendar = Calendar.getInstance()
                selectedCalendar.time = thisDate
                val selectedDate = selectedCalendar.timeInMillis

//                Realm.init(getApplicationContext())
//                val realmConfig = RealmConfiguration.Builder()
//                        .deleteRealmIfMigrationNeeded()
//                        .build()
//                realm = Realm.getInstance(realmConfig)

                selectedDateLabel.text = DateFormat.format("yyyy/MM/dd/mm/ss", selectedDate) // 선택한 날짜 라벨링
                toolbar.title = DateFormat.format("yyyy/MM", selectedDate) // 옆으로 스크롤하면 월 바뀜
//                list.layoutManager = LinearLayoutManager(this) // list Setting

                compactcalendar_view.setFirstDayOfWeek(1) // CalendarView Initializing
                compactcalendar_view.removeAllEvents()
//                var schedules = realm.where<Schedule>().findAll()
//                for (schedule in schedules) {
//                    val event = Event(Color.GREEN, schedule.startTime)
//                    compactcalendar_view.addEvent(event)
//                }



//                var todoList = arrayListOf<Schedule>(
//                    Schedule("18:00", "asdf"),
//                    Schedule("19:00", "qwer"),
//                )
//
//                val mAdapter = ScheduleAdapter(todoList)
//                val layoutManager = LinearLayoutManager(this)
//                recyclerView.layoutManager = layoutManager
//                recyclerView.adapter = mAdapter

                var scheduleList = arrayListOf<Schedule>()
//
//                db.collection("Chats").document(FamilyName.toString()).collection("CALENDAR")
//
                val mAdapter = ScheduleAdapter(scheduleList)
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = mAdapter


//                schedules = realm.where<Schedule>()
//                    .greaterThanOrEqualTo("startTime", selectedDate)
//                    .lessThan("startTime", selectedDate + 24 * 60 * 60 * 1000)
//                    .findAll()
//                    .sort("startTime")
//                var adapter = ScheduleAdapter(schedules)
//                list.adapter = adapter


                compactcalendar_view.setListener( // 캘린더 구경할때 사용
                    object : CompactCalendarView.CompactCalendarViewListener {
                        override fun onDayClick(dateClicked: Date) {
                            selectedCalendar.time = dateClicked
                            val selectedTimeInMills = selectedCalendar.timeInMillis
//                            val dateFormat = DateFormat.format("yyyyMMddmmss", selectedTimeInMills)
                            val dateFormat = DateFormat.format("yyyyMMdd", selectedTimeInMills)
                            selectedDateLabel.text = dateFormat

                            scheduleList.clear()
                            db.collection("Chats").document(FamilyName.toString()).collection("CALENDAR")       //DB에서 캘린더 가져오기
                                .document(selectedDateLabel.text.toString()).collection(selectedDateLabel.text.toString())
                                .get()
                                .addOnSuccessListener { documents ->

                                    for (document in documents) {
                                        db.collection("Chats").document(FamilyName.toString()).collection("CALENDAR")
                                            .document(selectedDateLabel.text.toString())
                                            .collection(selectedDateLabel.text.toString()).document(document.id.toString())
                                            .get()
                                            .addOnSuccessListener { docs ->
                                                scheduleList.add(Schedule(docs.data?.get("title").toString(), docs.data?.get("detail").toString()))     // 리스트 형태로 어뎁터에 넣어주기
                                                val mAdapter = ScheduleAdapter(scheduleList)    // 스케줄 보여주기
                                                recyclerView.adapter = mAdapter

                                                mAdapter.setItemClickListener( object : ScheduleAdapter.ItemClickListener{  // 해당 어뎁터 눌렀을때 -> 삭제
                                                    override fun onClick(view: View, position: Int) {
//                                                        Log.d("SSS", "${position}번 리스트 선택")

                                                        scheduleDialog(selectedDateLabel.text.toString(), scheduleList[position].sche_title.toString(), FamilyName.toString())  //스케줄 삭제 다이어로그


                                                    }
                                                })
                                            }
                                    }








                                    }

                        }

                        override fun onMonthScroll(firstDayOfNewMonth: Date?) { // 달이 바뀌면 그 달의 첫번째 날을 setting 
                            toolbar.title = DateFormat.format("yyyy/MM", firstDayOfNewMonth)
                        }
                    })


                fab.setOnClickListener { view -> // 작성 or 수정으로 이동
                    val intent = Intent(this, ScheduleEditActivity::class.java)
                    intent.putExtra("selected_date", selectedDateLabel.text.toString()) // 날짜
                    intent.putExtra("FamilyName", FamilyName) // FamilyName
                    startActivity(intent)
                }


//                adapter.setOnItenClickListener { id -> // adapter 클릭하면 그 일정 수정할 수 있도록
//                    val intent = Intent(this, ScheduleEditActivity::class.java)
//                        .putExtra("schedule_id", id)
//                    startActivity(intent)
//                }
            }





        /********************앨범 album**********************/
        var mutableAlbumList: MutableList<String> = mutableListOf("a")
        mutableAlbumList.clear()
        var mutableAlbumCoverList: MutableList<String> = mutableListOf("a")
        mutableAlbumCoverList.clear()
        db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
            .get()
            .addOnSuccessListener { documents ->
                for (document_acc in documents) {
                    mutableAlbumCoverList.add(document_acc.id)
                    document_acc.id.replace(FamilyName.toString(), "")
                        .replace("_", "")
                    var str = document_acc.id.split("년")
                    mutableAlbumList.add(str[0])


                }

                var mutableYear = mutableAlbumList.distinct()     //중복 제거

//                for (i in 0..(mutableYear.size -1)){
//                    db.collection("Chats").document(FamilyName.toString()).collection("BOARD")      //앨범 커버 사진
//                            .get()
//                            .addOnSuccessListener { document2 ->
//                                for (document_acc in document2) {       //해당 년도 가져오기
//                                    if(document_acc.id.contains(mutableYear[i].toString())){
//                                        mutableAlbumCoverList.add(document_acc.id)
//                                    }
//                                }
//
//
//                }





                Log.d("dddddddddd",mutableYear.toString())
                for (i in 0..(mutableYear.size - 1)) { // 거꾸로

                    val layoutInflater =
                        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val containView = layoutInflater.inflate(
                        R.layout.activity_album_card,
                        null
                    )
                    album_layout.addView(containView)

                    val ContentView = containView as View
                    var albumCardbtn = ContentView.findViewById(R.id.albumImageView) as ImageView
                    var albumCardtxt = ContentView.findViewById(R.id.info_text) as TextView
                    var albumCardLayout = ContentView.findViewById(R.id.album_LinearLayout) as LinearLayout
                    var account_uid: String = ""

                    albumCardtxt.setText(mutableYear[i].toString())

                    ////////////////////////// 앨범 커버
                    var imageName =
                            "gs://cacafirebase-554ac.appspot.com/Family_Board/" + FamilyName + "_" + mutableAlbumCoverList[i]
                    Log.d("albumImage", imageName)
                    val storage = Firebase.storage
                    var familyImgRef = storage.getReferenceFromUrl(imageName)
                    familyImgRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                        val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                        albumCardbtn.setImageBitmap(profilebmp)
                    }







                    albumCardLayout?.setOnClickListener() { // 해당 년도로 이동
                        val intent = Intent(application, AlbumActivity::class.java) // 인텐트 옮기기

//                        // 해당 년도의 photo 여부를 먼저 판단해서 넘김.
//                        var AlbumList1: MutableList<String> = mutableListOf("a") // 모든 게시판
//                        AlbumList1.clear()
//                        var AlbumList2: MutableList<String> = mutableListOf("a") // 앨범 있는 게시판
//                        AlbumList2.clear()
//
//                        db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
//                            .get()
//                            .addOnSuccessListener { document2 ->
//                                for (document_acc in document2) {//해당 년도 가져오기
////                                    AlbumList1.add(document_acc.id) // 해당 년도의 모든 게시판 가져오기
//
//                                    if (document_acc.id.toString().contains(mutableYear[i].toString(), true)) {
//                                        Log.d("AlbumList1", document_acc.id.toString()) // 해당 년도의 전체
//                                        AlbumList1.add(document_acc.id.toString()) // 해당 년도의 모든 게시판 가져오기
//                                    }else{
//                                        Log.d("AlbumList1", "NOPE") // 해당 년도의 전체
//                                    }
//
//                                }
//                            }

//                        Log.d("AlbumList1", mutableYear[i].toString()) // 해당 년도의 전체
//                        Log.d("AlbumList11", AlbumList1.toString()) // 해당 년도의 전체




                        intent.putExtra("FamilyName", FamilyName)
                        intent.putExtra("albumYear", mutableYear[i])

                        startActivity(intent)


//                        for (i in 0..(AlbumList1.size - 1)){
//                            db.collection("Chats").document(FamilyName.toString()).collection("BOARD").document(AlbumList1[i])
//                                    .get()
//                                    .addOnSuccessListener { document2 ->
//                                        if (document2 != null) {
//                                            if (document2.data?.get("photo").toString() == "true") { // photo 가 있을 경우
//                                                Log.d("AlbumList11", AlbumList1[i].toString()) // 해당 년도의 전체
//                                                AlbumList2.add(AlbumList1[i].toString())
//                                            }else{
//
//                                            }
//
//                                        }
//
//                                        if(i == (AlbumList1.size - 2)){ // 마지막까지 for문 돌면 그때 넘기기
//                                            Log.d("AlbumList222", AlbumList2.toString()) // 해당 년도의 전체
//                                            intent.putExtra("PhtoAlbumList", AlbumList2.toString()) // Phto List를 String으로 가져가기
//                                            startActivity(intent)
//                                        }
//                                    }
//                        }
                    }

                }

//                for (i in 0..mutableAlbumList.size -1){
//                    val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                    val containView = layoutInflater.inflate(
//                            R.layout.activity_album_card,
//                            null
//                    )
//                    val ContentView = containView as View
//                    var albumCardLayout = ContentView.findViewById(R.id.albumImageView) as ImageView
//                    var imageName =
//                            "gs://cacafirebase-554ac.appspot.com/Family_Board/" + FamilyName + "_" + mutableAlbumList[i]
//                    Log.d("familyimageName", imageName)
//                    val storage = Firebase.storage
//                    var familyImgRef = storage.getReferenceFromUrl(imageName)
//                    familyImgRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
//                        val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
//                        albumCardLayout.setImageBitmap(profilebmp)
//                        finish()
//                    }
//
//                }

            }



    }

    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun scheduleDialog(selectedDate : String, scheduleDoc : String, FamilyName : String){       //스케줄 삭제하기
        val dlg_schedule: AlertDialog.Builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
        dlg_schedule.setTitle("항목 삭제") //제목
        dlg_schedule.setMessage(scheduleDoc + "를 정말 삭제하시겠습니까?") // 메시지
        dlg_schedule.setPositiveButton(
            "확인",
            DialogInterface.OnClickListener { dialog, which ->
                // DB 삭제
                var fbAuth = FirebaseAuth.getInstance()
                val db: FirebaseFirestore = Firebase.firestore

                db.collection("Chats").document(FamilyName.toString()).collection("CALENDAR")
                    .document(selectedDate)
                    .collection(selectedDate).document(scheduleDoc)
                    .delete()

            })
        dlg_schedule.setNegativeButton(
            "취소",
            DialogInterface.OnClickListener { dialog, which ->
                // 취소
            })
        dlg_schedule.show()
    }
}
