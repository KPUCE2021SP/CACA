package com.example.myapplication


import android.app.AlertDialog
import android.app.TabActivity
import android.content.*
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
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
import com.example.myapplication.Home_Board.BoardActivity
import com.example.myapplication.Home_Board.BoardContentActivity
import com.example.myapplication.Mypage.MypageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
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
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import android.content.Intent
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.CalendarKotlin.Schedule
import com.example.myapplication.CalendarKotlin.ScheduleAdapter
import com.example.myapplication.CalendarKotlin.ScheduleEditActivity
import com.example.myapplication.R
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_home.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_schedule_content.*
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import kotlinx.android.synthetic.main.activity_schedule_main.*
import kotlinx.android.synthetic.main.activity_schedule_main.fab
import java.util.*

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

    fun onClick_clipboard(texttext: String) { // 클립 보드에 복사

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("라벨", texttext)
        clipboardManager!!.setPrimaryClip(clipData)

        Toast.makeText(applicationContext, "$texttext 복사완료", Toast.LENGTH_LONG).show()
    }


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
            LinearMainpage.visibility = View.GONE
            LinearMain1.visibility = View.VISIBLE
            LinearMain2.visibility = View.GONE
            LinearMain3.visibility = View.GONE
            LinearMain4.visibility = View.GONE
            LinearMain5.visibility = View.GONE
            btnMain1.setTextColor(Color.WHITE)
        }
        btnMain2.setOnClickListener {
            LinearMainpage.visibility = View.GONE
            LinearMain1.visibility = View.GONE
            LinearMain2.visibility = View.VISIBLE
            LinearMain3.visibility = View.GONE
            LinearMain4.visibility = View.GONE
            LinearMain5.visibility = View.GONE
            btnMain1.setTextColor(Color.WHITE)
        }
        btnMain3.setOnClickListener {

            LinearMainpage.visibility = View.GONE
            LinearMain1.visibility = View.GONE
            LinearMain2.visibility = View.GONE
            LinearMain3.visibility = View.VISIBLE
            LinearMain4.visibility = View.GONE
            LinearMain5.visibility = View.GONE
        }
        btnMain4.setOnClickListener {
            LinearMainpage.visibility = View.GONE
            LinearMain1.visibility = View.GONE
            LinearMain2.visibility = View.GONE
            LinearMain3.visibility = View.GONE
            LinearMain4.visibility = View.VISIBLE
            LinearMain5.visibility = View.GONE
            btnMain1.setTextColor(Color.WHITE)
        }
        btnMain5.setOnClickListener {
            LinearMainpage.visibility = View.GONE
            LinearMain1.visibility = View.GONE
            LinearMain2.visibility = View.GONE
            LinearMain3.visibility = View.GONE
            LinearMain4.visibility = View.GONE
            LinearMain5.visibility = View.VISIBLE
            btnMain1.setTextColor(Color.WHITE)
        }


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
                        Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT)
                                .show()
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
                                Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT)
                                    .show()
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
                        Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                    }


                    var custom_eye = docName.data?.get("customEye").toString()
                    val eyeName =
                        "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_eye
                    val customRef_eye = storage.getReferenceFromUrl(eyeName)
                    customRef_eye?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                        val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                        cu_eye_Iv.setImageBitmap(customRef)
                    }?.addOnFailureListener {
                        Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }






        btnMyPage.setOnClickListener {
            val intent = Intent(application, MypageActivity::class.java)
            startActivity(intent)
        }

        btnGroup.setOnClickListener {
            val intent = Intent(application, DynamicLinkActivity::class.java)
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
                    var notice_profile =
                        ContentView.findViewById(R.id.notice_profile) as ImageView // profile Image
                    var notice_image =
                        ContentView.findViewById(R.id.notice_image) as ImageView // Board Image
                    var notice_card_Layout =
                        ContentView.findViewById(R.id.notice_card_Layout) as LinearLayout


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
                                    Toast.makeText(
                                        this,
                                        "image downloade failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
                                    Toast.makeText(
                                        this,
                                        "image downloade failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
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

                            } else {
                                Log.d(ContentValues.TAG, "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }




                    notice_card_Layout?.setOnClickListener() { // 삭제

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
                                        Toast.makeText(
                                            this,
                                            "image downloade failed",
                                            Toast.LENGTH_SHORT
                                        ).show()
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
                                        Toast.makeText(
                                            this,
                                            "image downloade failed",
                                            Toast.LENGTH_SHORT
                                        ).show()
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

                                } else {
                                    Log.d(ContentValues.TAG, "No such document")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(ContentValues.TAG, "get failed with ", exception)
                            }
                        notice_card_Layout?.setOnClickListener() { // 삭제

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
                                Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT)
                                    .show()

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


// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar// Calendar

                val thisDate = Date()
                thisDate.hours = 0
                thisDate.minutes = 0
                thisDate.seconds = 0
                selectedCalendar = Calendar.getInstance()
                selectedCalendar.time = thisDate
                val selectedDate = selectedCalendar.timeInMillis

                selectedDateLabel.text = DateFormat.format("yyyy/MM/dd", selectedDate) // 선택한 날짜 라벨링
                toolbar.title = DateFormat.format("yyyy/MM", selectedDate) // 옆으로 스크롤하면 월 바뀜

//                val realmConfig = RealmConfiguration.Builder()
//                    .deleteRealmIfMigrationNeeded()
//                    .build()
//                realm = Realm.getInstance(realmConfig)

                list.layoutManager = LinearLayoutManager(this) // list Setting

                compactcalendar_view.setFirstDayOfWeek(1) // CalendarView Setting
                compactcalendar_view.removeAllEvents()
                var schedules = realm.where<Schedule>().findAll()
                for (schedule in schedules) {
                    val event = Event(Color.GREEN, schedule.startTime)
                    compactcalendar_view.addEvent(event)
                }

//                schedules = realm.where<Schedule>()
//                    .greaterThanOrEqualTo("startTime", selectedDate)
//                    .lessThan("startTime", selectedDate + 24 * 60 * 60 * 1000)
//                    .findAll()
//                    .sort("startTime")
//                var adapter = ScheduleAdapter(schedules)
//                list.adapter = adapter


                compactcalendar_view.setListener(
                    object : CompactCalendarView.CompactCalendarViewListener {
                        override fun onDayClick(dateClicked: Date) {
                            selectedCalendar.time = dateClicked
                            val selectedTimeInMills = selectedCalendar.timeInMillis
                            val dateFormat = DateFormat.format("yyyy/MM/dd", selectedTimeInMills)
                            selectedDateLabel.text = dateFormat

                            // 1. DB에 내용 있는지 확인해서 불러오기

                            // 2. 해당 날짜의 Edit으로 이동하기

//                            schedules = realm.where<Schedule>()
//                                .greaterThanOrEqualTo("startTime", selectedTimeInMills)
//                                .lessThan("startTime", selectedTimeInMills + 24 * 60 * 60 * 1000)
//                                .findAll()
//                                .sort("startTime")
//                            adapter = ScheduleAdapter(schedules)
//                            list.adapter = adapter
//                            adapter.setOnItenClickListener { id ->
//                                val intent =
//                                    Intent(this@HomeActivity, ScheduleEditActivity::class.java)
//                                        .putExtra("schedule_id", id)
//                                startActivity(intent)
//                            }
                        }

                        override fun onMonthScroll(firstDayOfNewMonth: Date?) { // 달이 바뀌면 그 달의 첫번째 날을 setting 
                          toolbar.title = DateFormat.format("yyyy/MM", firstDayOfNewMonth)
                        }
                    })


                fab.setOnClickListener { view -> // 작성 or 수정으로 이동
                    val intent = Intent(this, ScheduleEditActivity::class.java)
                        .putExtra("selected_date", selectedDateLabel.text.toString())
                    startActivity(intent)
                }
//                adapter.setOnItenClickListener { id -> // adapter 클릭하면 그 일정 수정할 수 있도록
//                    val intent = Intent(this, ScheduleEditActivity::class.java)
//                        .putExtra("schedule_id", id)
//                    startActivity(intent)
//                }
            }
    }
}
