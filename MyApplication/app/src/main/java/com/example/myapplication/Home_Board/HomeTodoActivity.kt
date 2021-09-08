package com.example.myapplication.Home_Board

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home_todo.*
import kotlinx.android.synthetic.main.todo_left.*
import kotlinx.android.synthetic.main.todo_right.*

class HomeTodoActivity : AppCompatActivity() {
    val db: FirebaseFirestore = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_todo)
        var fbAuth = FirebaseAuth.getInstance()
        var uid = fbAuth?.uid.toString()
        val FamilyName = intent.getStringExtra("FamilyName")
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

    }
}