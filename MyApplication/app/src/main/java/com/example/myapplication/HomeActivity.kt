package com.example.myapplication


import android.app.AlertDialog
import android.app.TabActivity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
import android.os.Bundle
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class HomeActivity : TabActivity() {
    var mutableList: MutableList<String> = mutableListOf("a")
    var mutableUIDList: MutableList<String> = mutableListOf("null")
    var mutableList1: MutableList<String> = mutableListOf("a")
    val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val FamilyName = intent.getStringExtra("FamilyName") // 제목 선정
        FamilyNameTextView.text = FamilyName


        var fbAuth = FirebaseAuth.getInstance() // 로그인
        var fbFire = FirebaseFirestore.getInstance()
        var uid = fbAuth?.uid.toString() // uid

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

            srl_Mainpage.isRefreshing = false // 인터넷 끊기
        }


        // random pick game --------------------------------- minigame

        mutableList1.clear()
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
//        val layouts : Array<LinearLayout> = arrayOf(layout_input3, layout_input4, layout_input5, layout_input6,
//            layout_input7, layout_input8)



//        btn_add.setOnClickListener {
//            if (layoutNumber == layouts.size) {
//                Toast.makeText(this,"최대 8개까지 추가 가능합니다.",Toast.LENGTH_SHORT).show()
//            } else {
//                layouts[layoutNumber].visibility = View.VISIBLE
//                layoutNumber += 1
//            }
//        }

//        btn_delete.setOnClickListener {
//            val candidate3 = edit_input3.text
//            val candidate4 = edit_input4.text
//            val candidate5 = edit_input5.text
//            val candidate6 = edit_input6.text
//            val candidate7 = edit_input7.text
//            val candidate8 = edit_input8.text
//            val candidates = arrayOf(candidate3, candidate4, candidate5, candidate6,
//                candidate7, candidate8)
//
//            if (layoutNumber == 0) {
//                Toast.makeText(this,"최소 2개까지 삭제 가능합니다.",Toast.LENGTH_SHORT).show()
//            } else {
//                layouts[layoutNumber - 1].visibility = View.GONE
//                layoutNumber -= 1
//                candidates[layoutNumber].clear()
//            }
//        }

        btn_pick.setOnClickListener {
            text_result.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25F)
            val candidates: Array<String> = editCandidate()
            val random = Random()
            var candidateNumber = 0

            if (candidates[0] == "" || candidates[1] == "") {
                Toast.makeText(this,"후보를 입력해주세요!",Toast.LENGTH_SHORT).show()
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
        }

        btn_one_pick.setOnClickListener {
            text_result.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20F)
            val candidates: Array<String> = editCandidate()
            var candidateNumber = 0

            if (candidates[0] == "" || candidates[1] == "") {
                Toast.makeText(this,"후보를 입력해주세요!",Toast.LENGTH_SHORT).show()
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
                    randomNum1 = (Math.random()*candidateNumber).toInt()
                    temp = candidates[randomNum1]
                    randomNum2 = (Math.random()*candidateNumber).toInt()
                    temp2 = candidates[randomNum2]
                    candidates[randomNum1] = temp2
                    candidates[randomNum2] = temp
                }

                text_result.text = "[1등] ${candidates[0]}"  + "\t\t\t\t[2등] ${candidates[1]}"
                for (i in 2..layoutNumber + 1) {
                    text_result.text = text_result.text.toString() + "\t\t\t\t[${i+1}등] ${candidates[i]}"
                }
            }

            val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            text_result.startAnimation(animationFadeIn)
        }

        // **--------------------------------- 용돈 관리 ----------------------------------------**
        Account_Plus_Button.setOnClickListener {                //계좌 추가하기
            val intent = Intent(application, AddAccount::class.java)
            intent.putExtra("FamilyName", FamilyName)
            startActivity(intent)
        }


        var mutableAccountList: MutableList<String> = mutableListOf("a")
        mutableAccountList.clear()
        db.collection("Chats").document(FamilyName.toString()).collection("ACCOUNT")
            .get()
            .addOnSuccessListener { documents ->
                for (document_acc in documents) {
                    mutableAccountList.add(document_acc.id.toString())
                }
                for (i in 0..(mutableAccountList.size - 1)) { // 거꾸로
                    val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val containView = layoutInflater.inflate(
                        R.layout.activity_account,
                        null
                    ) // mypage_content를 inflate
                    account_Layout.addView(containView)


                    val ContentView = containView as View
                    var account_name = ContentView.findViewById(R.id.account_name) as TextView // 이름
                    var account_bankName = ContentView.findViewById(R.id.account_bankName) as TextView // 계좌은행
                    var account_bankNum = ContentView.findViewById(R.id.account_bankNum) as TextView // 계좌 번호
                    var account_profile =
                        ContentView.findViewById(R.id.account_profile) as ImageView // profile Image
                    var account_uid : String = ""

                    val docRef1 =
                        db.collection("Chats").document(FamilyName.toString()).collection("ACCOUNT")
                            .document(mutableAccountList[(mutableAccountList.size - 1)-i]) // 여러 field값 가져오기
                    docRef1.get()
                        .addOnSuccessListener { document3 ->
                            if (document3 != null) {
                                Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document3.data}")
                                //textViewName.setText(document.data?.get("name").toString()) // name 확인용
                                account_bankName.setText(document3.data?.get("bankName").toString())
                                account_bankNum.setText(document3.data?.get("bankAccount").toString())

                                account_uid = document3.data?.get("uid").toString()
                                // profile Image
                                // document2.data?.get("uid").toString()
                                val imageName =
                                    "gs://cacafirebase-554ac.appspot.com/profiles/" + document3.data?.get(
                                        "uid"
                                    ).toString()
                                Log.d("imageName", imageName)
                                val storage = Firebase.storage
                                val storageRef = storage.reference
                                val profileRef1 = storage.getReferenceFromUrl(imageName)
                                profileRef1?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                    val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                                    account_profile.setImageBitmap(profilebmp) // 작성한 사람 uid로 profileImage 변경!
                                }?.addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "image downloade failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                // uid to Name
                                val docRef = db.collection("Member")
                                    .document(document3.data?.get("uid").toString())
                                docRef.get()
                                    .addOnSuccessListener { document ->
                                        if (document != null) {
                                            Log.d(
                                                ContentValues.TAG,
                                                "DocumentSnapshot data: ${document.data}"
                                            )
                                            account_name.setText(
                                                document.data?.get("name").toString()
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
                }





//                account_All?.setOnLongClickListener() { // 삭제
//
//                    val dlg: AlertDialog.Builder = AlertDialog.Builder(
//                        this,
//                        android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
//                    )
//                    dlg.setTitle("항목 삭제") //제목
//                    dlg.setMessage(account_name.text.toString() + "의 계좌를 정말 삭제하시겠습니까?") // 메시지
//                    dlg.setPositiveButton(
//                        "확인",
//                        DialogInterface.OnClickListener { dialog, which ->
//                            // DB 삭제
//                            var fbAuth = FirebaseAuth.getInstance()
//                            val db: FirebaseFirestore = Firebase.firestore
//
//                            val docRef = db.collection("Chats").document(FamilyName.toString())
//                                .collection("ACCOUNT").document(account_uid.toString())
//                                .delete()
//
//                        })
//                    dlg.setNegativeButton(
//                        "취소",
//                        DialogInterface.OnClickListener { dialog, which ->
//                            // 취소
//                        })
//                    dlg.show()
//                }
            }


    }




    private fun editCandidate(): Array<String> {
//        for(i in 0 .. mutableList1.size-1){
//            val candidate[i] = mutableList[i]
//        }
        val candidate1 = mutableList1[0]
        val candidate2 = mutableList1[1]
        return arrayOf(candidate1, candidate2)
//        val candidate3 = mutableList1[2]
//        val candidate4 = mutableList1[0]
//        val candidate5 = mutableList1[0]
//        val candidate6 = edit_input6.text.toString()
//        val candidate7 = edit_input7.text.toString()
//        val candidate8 = edit_input8.text.toString()
//        return arrayOf(candidate1, candidate2, candidate3, candidate4, candidate5,
//            candidate6, candidate7, candidate8)
    }
}