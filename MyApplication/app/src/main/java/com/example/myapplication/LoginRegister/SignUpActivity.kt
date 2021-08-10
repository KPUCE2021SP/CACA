package com.example.myapplication.LoginRegister

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.signuppage.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    // 파이어베이스 인증 객체 생성
    private var firebaseAuth: FirebaseAuth? = null
    var fbAuth = FirebaseAuth.getInstance() // 로그인
    var uid = fbAuth?.uid.toString() // uid

    // 이메일과 비밀번호
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var email = ""
    private var password = ""
    private var same : Int = 0
    val db: FirebaseFirestore = Firebase.firestore
    val docRef1 = db.collection("Member").document(uid)

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.signuppage)

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.et_eamil)
        editTextPassword = findViewById(R.id.et_password)


        btn_signUp.setOnClickListener {
            editFull()
        }
        et_passwordConfirm.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                samePwd()
            }

            else{
                samePwd()
            }
        }

    }


    private fun samePwd(){  //비밀번호 일치하는지 확인
        if (et_password.text.toString() != et_passwordConfirm.text.toString() || et_password.getText().toString().equals("")){
            warning_pwd.setText("비밀번호가 일치하지 않습니다")
            warning_pwd.setTextColor(Color.RED)
        } else {
            warning_pwd.setText("비밀번호가 일치합니다")
            warning_pwd.setTextColor(Color.BLUE)
            same = 1
        }
    }


    private fun editFull() { //모두 다 입력했는지 확인
        if (et_eamil.getText().toString().equals("") || et_password.getText().toString().equals("") ||
                et_passwordConfirm.getText().toString().equals("") || et_birthday.getText().toString().equals("") ||
                et_address.getText().toString().equals("") || et_phone.getText().toString().equals("")) {
            samePwd()
            Toast.makeText(applicationContext, "다시 한번 확인해주세요", Toast.LENGTH_SHORT).show()
        } else {
            samePwd()
            if (et_birthday.length() == 8) {        //생년월일 확인
                if (et_phone.length() == 11) {          //전화번호 확인
                    if (same == 1) {        //비밀번호확인 일치
                        signUp()


                    }
                } else {
                    Toast.makeText(applicationContext, "전화번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "생년월일을 다시 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun signUp() {
        email = editTextEmail!!.text.toString()
        password = editTextPassword!!.text.toString()
        if (isValidEmail && isValidPasswd) {
            createUser(email, password)
        } else {
            warning_pwd_pattern.setTextColor(Color.RED)
        }
    }

    // 이메일 유효성 검사
    private val isValidEmail: Boolean
        private get() = if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(applicationContext, "이메일 공백 에러", Toast.LENGTH_SHORT).show()

            // 이메일 공백
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(applicationContext, "이메일 형식 불일치 에러", Toast.LENGTH_SHORT).show()

            // 이메일 형식 불일치
            false
        } else {
            true
        }// 비밀번호 형식 불일치// 비밀번호 공백


    // 비밀번호 유효성 검사
    private val isValidPasswd: Boolean
        private get() {
            return if (password.isEmpty()) {
                Toast.makeText(applicationContext, "비밀번호 공백 에러", Toast.LENGTH_SHORT).show()
                // 비밀번호 공백
                false
            } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                Toast.makeText(applicationContext, "비밀번호 형식 불일치 에러", Toast.LENGTH_SHORT).show()

                // 비밀번호 형식 불일치
                false
            } else {
                true
            }
        }

    // 회원가입
    private fun createUser(email: String, password: String) {
        firebaseAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful) {
                    // 회원가입 성공
//                    var fbAuth = FirebaseAuth.getInstance() // 로그인
//                    var uid = fbAuth?.uid.toString() // uid
                    saveDB(fbAuth?.uid.toString())
                    Toast.makeText(applicationContext,"회원가입 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // 회원가입 실패
                    Toast.makeText(applicationContext,"회원가입 실패!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveDB(a : String){               //DB에 넣는 함수
        val human = hashMapOf(      //db에 넣기
                "name" to et_name.text.toString(),
                "birthday" to et_birthday.text.toString(),
                "phone" to et_phone.text.toString(),
                "address" to et_address.text.toString(),
        )
        val family = hashMapOf(
                "null" to "null"
        )

        db.collection("Member").document(a).set(human) // db에 넣기
        db.collection("Member").document(a).collection("MYPAGE").document("MYPAGE").set(family)

    }

    companion object {
        // 비밀번호 정규식
        private val PASSWORD_PATTERN =
            Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")
    }
}