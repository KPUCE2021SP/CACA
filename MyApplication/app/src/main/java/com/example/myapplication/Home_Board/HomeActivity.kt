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
//import kotlinx.android.synthetic.main.activity_home.btnSend
//import kotlinx.android.synthetic.main.activity_home.etMessage
//import kotlinx.android.synthetic.main.activity_home.etTitle
//import kotlinx.android.synthetic.main.activity_home.etToken

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

    // ??? // Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar // ???

    fun onClick_clipboard(texttext: String) { // ?????? ????????? ??????

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("??????", texttext)
        clipboardManager!!.setPrimaryClip(clipData)

        Toast.makeText(applicationContext, "$texttext ????????????", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val FamilyName = intent.getStringExtra("FamilyName") // ?????? ??????
        FamilyNameTextView.text = FamilyName


        var fbAuth = FirebaseAuth.getInstance() // ?????????
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
                        document.data?.get("name").toString() // family name ??????

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }


        //val tabHost = this.tabHost

        val tabSpecHome = tabhost.newTabSpec("HOME").setIndicator("??????")
        tabSpecHome.setContent(R.id.tabHome)
        tabhost.addTab(tabSpecHome)

        val tabSpecBoard = tabhost.newTabSpec("BOARD").setIndicator("?????????")
        tabSpecBoard.setContent(R.id.tabBoard)
        tabhost.addTab(tabSpecBoard)

        val tabSpecCalendar = tabhost.newTabSpec("CALENDAR").setIndicator("??????")
        tabSpecCalendar.setContent(R.id.tabCalendar)

        tabhost.addTab(tabSpecCalendar)

        val tabSpecAlbum = tabhost.newTabSpec("ALBUM").setIndicator("??????")
        tabSpecAlbum.setContent(R.id.tabAlbum)
        tabhost.addTab(tabSpecAlbum)

        tabhost.currentTab = 0

        btnMain1.setOnClickListener {
            val intent = Intent(application, HomeAccountActivity::class.java)
            intent.putExtra("FamilyName", FamilyName)
            startActivity(intent)
        }
        btnMain2.setOnClickListener {
            val intent = Intent(application, HomeDialogActivity::class.java)
            intent.putExtra("FamilyName", FamilyName)
            startActivity(intent)
        }
        btnMain3.setOnClickListener {
            val intent = Intent(application, HomeMiniGameActivity::class.java)
            intent.putExtra("FamilyName", FamilyName)
            startActivity(intent)
        }
        btnMain4.setOnClickListener {
            val intent = Intent(application, HomeTodoActivity::class.java)
            intent.putExtra("FamilyName", FamilyName)
            intent.putExtra("UserUID",uid)
            startActivity(intent)
        }
        btnMain5.setOnClickListener {
            val intent = Intent(application, HomeMessageActivity::class.java)
            intent.putExtra("FamilyName", FamilyName)
            startActivity(intent)
        }


        /***************?????? ????????? ???????????? ?????? ?????? ??????********************/
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

//                    var emotion = docName.data?.get("emotion").toString()
//                    val bodyEmotion = "gs://cacafirebase-554ac.appspot.com/custom_image/emotion/" + emotion
//                    val customRef_Emo = storage.getReferenceFromUrl(bodyEmotion)
//                    customRef_Emo?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
//                        val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
//                        cu_body_Iv.setImageBitmap(customRef)
//                    }?.addOnFailureListener {
//                        //     Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT)
//                        //.show()
//                    }


                    customImage.setOnClickListener {    //?????????
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

                    var custom_emotion = docName.data?.get("emotion").toString()
                    val emotionName =
                        "gs://cacafirebase-554ac.appspot.com/custom_image/emotion/" + custom_emotion
                    val customRef_emt = storage.getReferenceFromUrl(emotionName)
                    customRef_emt?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                        val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                        cu_eye_Iv.setImageBitmap(customRef)
                    }?.addOnFailureListener {
                   //     Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                    }


                }
            }
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

        Board_Plus_Button.setOnClickListener() { // ????????? ??? ???????????? ???????????? ??????
            // board Format
            val current = LocalDateTime.now() // ??? ????????? ?????? ????????????
            val formatter = DateTimeFormatter.ofPattern("yyyy??? MM??? dd??? HH??? mm??? ss???")
            val formatted = current.format(formatter)
//            var PhotoBoolean : Boolean = false // ?????? ?????? ??????
            val board_format = hashMapOf(
                // Family name
                "location" to ""
            )
            db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                .document(formatted)
                .set(board_format as Map<String, Any>)//.set(board_content) // ????????? ?????????


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


                for (i in 0..(mutableList.size - 1)) { // ?????????
                    val layoutInflater =
                        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val containView = layoutInflater.inflate(
                        R.layout.notice_card,
                        null
                    ) // mypage_content??? inflate
                    Board_LinearLayout.addView(containView)

                    notice_image.background =
                        getResources().getDrawable(R.drawable.imageview_cornerround, null)
                    notice_image.setClipToOutline(true)

                    val ContentView = containView as View
                    var notice_board = ContentView.findViewById(R.id.notice_board) as TextView // ??????
                    var notice_time = ContentView.findViewById(R.id.notice_time) as TextView // ??????
                    var notice_name = ContentView.findViewById(R.id.notice_name) as TextView // uid
                    var notice_comment = ContentView.findViewById(R.id.notice_comment) as TextView // uid
                    var notice_profile =
                        ContentView.findViewById(R.id.notice_profile) as ImageView // profile Image
                    var notice_image =
                        ContentView.findViewById(R.id.notice_image) as ImageView // Board Image
                    var notice_card_Layout =
                        ContentView.findViewById(R.id.notice_card_Layout) as LinearLayout
                    var notice_delete_button = ContentView.findViewById(R.id.notice_card_delete) as ImageView // ????????????



                    // ?????? ??????
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
                            .document(mutableList[(mutableList.size - 1) - i]) // ?????? field??? ????????????
                    docRef1.get()
                        .addOnSuccessListener { document2 ->
                            if (document2 != null) {
                                Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document2.data}")
                                //textViewName.setText(document.data?.get("name").toString()) // name ?????????
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
                                    notice_profile.setImageBitmap(profilebmp) // ????????? ?????? uid??? profileImage ??????!
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
                                    notice_image.setImageBitmap(profilebmp2) // ????????? ?????? uid??? profileImage ??????!
                                }?.addOnFailureListener {
//                                    Toast.makeText(
//                                        this,
//                                        "image downloade failed",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
                                    notice_image.isGone =
                                        true                            // ???????????? ???????????? ?????????
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
                                            ) // name ?????????

                                        } else {
                                            Log.d(ContentValues.TAG, "No such document")
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d(ContentValues.TAG, "get failed with ", exception)
                                    }
                                /////////////////////////// ????????? ???????????? intent
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




                    notice_delete_button?.setOnClickListener() { // ??????

                        val dlg: AlertDialog.Builder = AlertDialog.Builder(
                            this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                        )
                        dlg.setTitle("?????? ??????") //??????
                        dlg.setMessage(notice_time.text.toString() + "??? ?????? ?????????????????????????") // ?????????
                        dlg.setPositiveButton(
                            "??????",
                            DialogInterface.OnClickListener { dialog, which ->
                                // DB ??????
                                var fbAuth = FirebaseAuth.getInstance()
                                val db: FirebaseFirestore = Firebase.firestore

                                val docRef = db.collection("Chats").document(FamilyName.toString())
                                    .collection("BOARD").document(notice_time.text.toString())
                                    .delete()

                            })
                        dlg.setNegativeButton(
                            "??????",
                            DialogInterface.OnClickListener { dialog, which ->
                                // ??????
                            })
                        dlg.show()
                    }
                }
                notice_image.background =
                    getResources().getDrawable(R.drawable.imageview_cornerround, null)
                notice_image.setClipToOutline(true)
            }




        //????????? ??????????????????
        srl_Mainpage.setOnRefreshListener {
            // ????????? ?????? ??????
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


                    for (i in 0..(mutableList.size - 1)) { // ?????????
                        val layoutInflater =
                            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val containView = layoutInflater.inflate(
                            R.layout.notice_card,
                            null
                        ) // mypage_content??? inflate
                        Board_LinearLayout.addView(containView)

                        val ContentView = containView as View
                        var notice_board =
                            ContentView.findViewById(R.id.notice_board) as TextView // ??????
                        var notice_time =
                            ContentView.findViewById(R.id.notice_time) as TextView // ??????
                        var notice_name =
                            ContentView.findViewById(R.id.notice_name) as TextView // uid
                        var notice_profile =
                            ContentView.findViewById(R.id.notice_profile) as ImageView // profile Image
                        var notice_image =
                            ContentView.findViewById(R.id.notice_image) as ImageView // Board Image
                        var notice_card_Layout =
                            ContentView.findViewById(R.id.notice_card_Layout) as LinearLayout

                        var notice_delete_button = ContentView.findViewById(R.id.notice_card_delete) as ImageView // ????????????

                        val docRef1 = db.collection("Chats").document(FamilyName.toString())
                            .collection("BOARD")
                            .document(mutableList[(mutableList.size - 1) - i]) // ?????? field??? ????????????
                        docRef1.get()
                            .addOnSuccessListener { document2 ->
                                if (document2 != null) {
                                    Log.d(
                                        ContentValues.TAG,
                                        "DocumentSnapshot data: ${document2.data}"
                                    )
                                    //textViewName.setText(document.data?.get("name").toString()) // name ?????????
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
                                        notice_profile.setImageBitmap(profilebmp) // ????????? ?????? uid??? profileImage ??????!
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
                                        notice_image.setImageBitmap(profilebmp2) // ????????? ?????? uid??? profileImage ??????!
                                    }?.addOnFailureListener {
//                                        Toast.makeText(
//                                            this,
//                                            "image downloade failed",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
                                        notice_image.isGone =
                                            true                            // ???????????? ???????????? ?????????
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
                                                ) // name ?????????

                                            } else {
                                                Log.d(ContentValues.TAG, "No such document")
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.d(ContentValues.TAG, "get failed with ", exception)
                                        }

                                    /////////////////////////// ????????? ???????????? intent
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
                        notice_delete_button?.setOnClickListener() { // ??????

                            val dlg: AlertDialog.Builder = AlertDialog.Builder(
                                this,
                                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                            )
                            dlg.setTitle("?????? ??????") //??????
                            dlg.setMessage(notice_time.text.toString() + "??? ?????? ?????????????????????????") // ?????????
                            dlg.setPositiveButton(
                                "??????",
                                DialogInterface.OnClickListener { dialog, which ->
                                    // DB ??????
                                    var fbAuth = FirebaseAuth.getInstance()
                                    val db: FirebaseFirestore = Firebase.firestore

                                    val docRef =
                                        db.collection("Chats").document(FamilyName.toString())
                                            .collection("BOARD")
                                            .document(notice_time.text.toString())
                                            .delete()

                                })
                            dlg.setNegativeButton(
                                "??????",
                                DialogInterface.OnClickListener { dialog, which ->
                                    // ??????
                                })
                            dlg.show()
                        }
                    }
                    notice_image.background =
                        getResources().getDrawable(R.drawable.imageview_cornerround, null)
                    notice_image.setClipToOutline(true)
                }

            srl_Mainpage.isRefreshing = false // ????????? ??????
        }


// Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger  // Messenger


//                var mutableListMessage: MutableList<String> = mutableListOf("a")
//                var mutableListMessageDEVICE: MutableList<String> = mutableListOf("a")
//                mutableListMessage.clear()
//                mutableListMessageDEVICE.clear()
//
//                db.collection("Chats").document(FamilyName.toString()).collection("FamilyMember")
//                    .get()
//                    .addOnSuccessListener { documents ->
//                        for (document in documents) {
//                            Log.d("FamilyMember", "${document.id}")
////                            mutableListMessageUID.add(document.id)
//                            // DEVICE
//                            val docRef3 = db.collection("Member").document(document.id).collection("DEVICE").document("TOKEN")
//                            docRef3.get()
//                                .addOnSuccessListener { docName ->
//                                    if (docName != null) {
//                                        mutableListMessageDEVICE.add(docName.data?.get("deviceInfo").toString())
//                                        Log.d("device", mutableListMessageDEVICE.toString())
//                                    } else {
//                                        Log.d(ContentValues.TAG, "No such document")
//                                    }
//                                }
//                                .addOnFailureListener { exception ->
//                                    Log.d(ContentValues.TAG, "get failed with ", exception)
//                                }
//
//
////                    //Member ?????? ???????????? mutableNameList??? ??????
//                            val docRef2 = db.collection("Member").document(document.id)
//                            docRef2.get()
//                                .addOnSuccessListener { docName ->
//                                    if (docName != null) {
//                                        Log.d(
//                                            ContentValues.TAG,
//                                            "DocumentSnapshot data: ${docName.data}"
//                                        )
//                                        mutableListMessage.add(docName.data?.get("name").toString())
//
//                                        var adapter: ArrayAdapter<String> = ArrayAdapter(
//                                            this,
//                                            android.R.layout.simple_spinner_dropdown_item,
//                                            mutableListMessage
//                                        )
//                                        spinner_message.adapter = adapter
//
//                                    } else {
//                                        Log.d(ContentValues.TAG, "No such document")
//                                    }
//                                }
//                                .addOnFailureListener { exception ->
//                                    Log.d(ContentValues.TAG, "get failed with ", exception)
//                                }
//                        }
//
//                    }
//
//
//                var spinnerUID: String = ""
//                spinner_message.setSelection(0, false)
//                spinner_message.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                    override fun onItemSelected(
//                        parent: AdapterView<*>?,
//                        view: View?,
//                        position: Int,
//                        id: Long
//                    ) {
//                        spinnerUID = mutableListMessageDEVICE[position].toString()
//                        etToken.setText(mutableListMessageDEVICE[position])
//                        Log.d("spinnerUID", spinnerUID)
//                    }
//
//                    override fun onNothingSelected(parent: AdapterView<*>?) {
//                        TODO("Not yet implemented")
//                    }
//                }
//
//                btnSend.setOnClickListener {
//                    val title = etTitle.text.toString()
//                    val message = etMessage.text.toString()
//                    val recipientToken = etToken.text.toString()
//                    if(title != "" && message != "" && recipientToken != "") {
//                        PushNotification(
//                            NotificationData(title, message),
//                            recipientToken
//                        )
//                        sendNotification(PushNotification(NotificationData(title, message),recipientToken))
//
//                    }
//                }




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

                selectedDateLabel.text = DateFormat.format("yyyy/MM/dd/mm/ss", selectedDate) // ????????? ?????? ?????????
                toolbar.title = DateFormat.format("yyyy/MM", selectedDate) // ????????? ??????????????? ??? ??????
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


                compactcalendar_view.setListener( // ????????? ???????????? ??????
                    object : CompactCalendarView.CompactCalendarViewListener {
                        override fun onDayClick(dateClicked: Date) {
                            selectedCalendar.time = dateClicked
                            val selectedTimeInMills = selectedCalendar.timeInMillis
//                            val dateFormat = DateFormat.format("yyyyMMddmmss", selectedTimeInMills)
                            val dateFormat = DateFormat.format("yyyyMMdd", selectedTimeInMills)
                            selectedDateLabel.text = dateFormat

                            scheduleList.clear()
                            db.collection("Chats").document(FamilyName.toString()).collection("CALENDAR")       //DB?????? ????????? ????????????
                                .document(selectedDateLabel.text.toString()).collection(selectedDateLabel.text.toString())
                                .get()
                                .addOnSuccessListener { documents ->

                                    for (document in documents) {
                                        db.collection("Chats").document(FamilyName.toString()).collection("CALENDAR")
                                            .document(selectedDateLabel.text.toString())
                                            .collection(selectedDateLabel.text.toString()).document(document.id.toString())
                                            .get()
                                            .addOnSuccessListener { docs ->
                                                scheduleList.add(Schedule(docs.data?.get("title").toString(), docs.data?.get("detail").toString()))     // ????????? ????????? ???????????? ????????????
                                                val mAdapter = ScheduleAdapter(scheduleList)    // ????????? ????????????
                                                recyclerView.adapter = mAdapter

                                                mAdapter.setItemClickListener( object : ScheduleAdapter.ItemClickListener{  // ?????? ????????? ???????????? -> ??????
                                                    override fun onClick(view: View, position: Int) {
//                                                        Log.d("SSS", "${position}??? ????????? ??????")

                                                        scheduleDialog(selectedDateLabel.text.toString(), scheduleList[position].sche_title.toString(), FamilyName.toString())  //????????? ?????? ???????????????


                                                    }
                                                })
                                            }
                                    }








                                    }

                        }

                        override fun onMonthScroll(firstDayOfNewMonth: Date?) { // ?????? ????????? ??? ?????? ????????? ?????? setting 
                            toolbar.title = DateFormat.format("yyyy/MM", firstDayOfNewMonth)
                        }
                    })


                fab.setOnClickListener { view -> // ?????? or ???????????? ??????
                    val intent = Intent(this, ScheduleEditActivity::class.java)
                    intent.putExtra("selected_date", selectedDateLabel.text.toString()) // ??????
                    intent.putExtra("FamilyName", FamilyName) // FamilyName
                    startActivity(intent)
                }


//                adapter.setOnItenClickListener { id -> // adapter ???????????? ??? ?????? ????????? ??? ?????????
//                    val intent = Intent(this, ScheduleEditActivity::class.java)
//                        .putExtra("schedule_id", id)
//                    startActivity(intent)
//                }
//            }





        /********************?????? album**********************/
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
                    var str = document_acc.id.split("???")
                    mutableAlbumList.add(str[0])


                }

                var mutableYear = mutableAlbumList.distinct()     //?????? ??????

//                for (i in 0..(mutableYear.size -1)){
//                    db.collection("Chats").document(FamilyName.toString()).collection("BOARD")      //?????? ?????? ??????
//                            .get()
//                            .addOnSuccessListener { document2 ->
//                                for (document_acc in document2) {       //?????? ?????? ????????????
//                                    if(document_acc.id.contains(mutableYear[i].toString())){
//                                        mutableAlbumCoverList.add(document_acc.id)
//                                    }
//                                }
//
//
//                }





                Log.d("dddddddddd",mutableYear.toString())
                for (i in 0..(mutableYear.size - 1)) { // ?????????

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

                    ////////////////////////// ?????? ??????
                    var imageName =
                            "gs://cacafirebase-554ac.appspot.com/Family_Board/" + FamilyName + "_" + mutableAlbumCoverList[i]
                    Log.d("albumImage", imageName)
                    val storage = Firebase.storage
                    var familyImgRef = storage.getReferenceFromUrl(imageName)
                    familyImgRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                        val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                        albumCardbtn.setImageBitmap(profilebmp)
                    }







                    albumCardLayout?.setOnClickListener() { // ?????? ????????? ??????
                        val intent = Intent(application, AlbumActivity::class.java) // ????????? ?????????

//                        // ?????? ????????? photo ????????? ?????? ???????????? ??????.
//                        var AlbumList1: MutableList<String> = mutableListOf("a") // ?????? ?????????
//                        AlbumList1.clear()
//                        var AlbumList2: MutableList<String> = mutableListOf("a") // ?????? ?????? ?????????
//                        AlbumList2.clear()
//
//                        db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
//                            .get()
//                            .addOnSuccessListener { document2 ->
//                                for (document_acc in document2) {//?????? ?????? ????????????
////                                    AlbumList1.add(document_acc.id) // ?????? ????????? ?????? ????????? ????????????
//
//                                    if (document_acc.id.toString().contains(mutableYear[i].toString(), true)) {
//                                        Log.d("AlbumList1", document_acc.id.toString()) // ?????? ????????? ??????
//                                        AlbumList1.add(document_acc.id.toString()) // ?????? ????????? ?????? ????????? ????????????
//                                    }else{
//                                        Log.d("AlbumList1", "NOPE") // ?????? ????????? ??????
//                                    }
//
//                                }
//                            }

//                        Log.d("AlbumList1", mutableYear[i].toString()) // ?????? ????????? ??????
//                        Log.d("AlbumList11", AlbumList1.toString()) // ?????? ????????? ??????




                        intent.putExtra("FamilyName", FamilyName)
                        intent.putExtra("albumYear", mutableYear[i])

                        startActivity(intent)


//                        for (i in 0..(AlbumList1.size - 1)){
//                            db.collection("Chats").document(FamilyName.toString()).collection("BOARD").document(AlbumList1[i])
//                                    .get()
//                                    .addOnSuccessListener { document2 ->
//                                        if (document2 != null) {
//                                            if (document2.data?.get("photo").toString() == "true") { // photo ??? ?????? ??????
//                                                Log.d("AlbumList11", AlbumList1[i].toString()) // ?????? ????????? ??????
//                                                AlbumList2.add(AlbumList1[i].toString())
//                                            }else{
//
//                                            }
//
//                                        }
//
//                                        if(i == (AlbumList1.size - 2)){ // ??????????????? for??? ?????? ?????? ?????????
//                                            Log.d("AlbumList222", AlbumList2.toString()) // ?????? ????????? ??????
//                                            intent.putExtra("PhtoAlbumList", AlbumList2.toString()) // Phto List??? String?????? ????????????
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

    fun scheduleDialog(selectedDate : String, scheduleDoc : String, FamilyName : String){       //????????? ????????????
        val dlg_schedule: AlertDialog.Builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
        dlg_schedule.setTitle("?????? ??????") //??????
        dlg_schedule.setMessage(scheduleDoc + "??? ?????? ?????????????????????????") // ?????????
        dlg_schedule.setPositiveButton(
            "??????",
            DialogInterface.OnClickListener { dialog, which ->
                // DB ??????
                var fbAuth = FirebaseAuth.getInstance()
                val db: FirebaseFirestore = Firebase.firestore

                db.collection("Chats").document(FamilyName.toString()).collection("CALENDAR")
                    .document(selectedDate)
                    .collection(selectedDate).document(scheduleDoc)
                    .delete()

            })
        dlg_schedule.setNegativeButton(
            "??????",
            DialogInterface.OnClickListener { dialog, which ->
                // ??????
            })
        dlg_schedule.show()
    }
}
