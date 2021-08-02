package com.example.myapplication

import android.content.Intent
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loginactivity.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    // 파이어베이스 인증 객체 생성
    private var firebaseAuth: FirebaseAuth? = null

    // 이메일과 비밀번호
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var email = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginactivity)

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.et_eamil)
        editTextPassword = findViewById(R.id.et_password)


        btn_signUp.setOnClickListener {
            signUp()
        }
        btn_signIn.setOnClickListener(){
            signIn()
        }
    }


    private fun signUp() {
        email = editTextEmail!!.text.toString()
        password = editTextPassword!!.text.toString()
        if (isValidEmail && isValidPasswd) {
            createUser(email, password)
        }
    }

    private fun signIn() {
        //fun signIn() {
        email = editTextEmail!!.text.toString()
        password = editTextPassword!!.text.toString()
        if (isValidEmail && isValidPasswd) {
            loginUser(email, password)
        }
    }// 이메일 형식 불일치// 이메일 공백

    // 이메일 유효성 검사
    private val isValidEmail: Boolean
        private get() = if (email.isEmpty()) {
            // 이메일 공백
            false
        } else Patterns.EMAIL_ADDRESS.matcher(email).matches()// 비밀번호 형식 불일치// 비밀번호 공백


    // 비밀번호 유효성 검사
    private val isValidPasswd: Boolean
        private get() {
            return if (password.isEmpty()) {
                // 비밀번호 공백
                false
            } else PASSWORD_PATTERN.matcher(password).matches()

        }

    // 회원가입
    private fun createUser(email: String, password: String) {
        firebaseAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful) {
                    // 회원가입 성공
                    Toast.makeText(
                        this@LoginActivity,
                        "회원가입 성공!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // 회원가입 실패
                    Toast.makeText(
                        this@LoginActivity,
                        "회원가입 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // 로그인
    private fun loginUser(email: String, password: String) {
        firebaseAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {

                    // 로그인 성공
                    Toast.makeText(
                        this@LoginActivity,
                        "로그인 성공!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(application, DynamicLinkActivity::class.java)
                    startActivity(intent)


                } else {
                    // 로그인 실패
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        // 비밀번호 정규식
        private val PASSWORD_PATTERN =
            Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")
    }
}







//import android.content.Intent
//import android.graphics.BitmapFactory
//import android.os.Bundle
//import android.os.PersistableBundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.ktx.storage
//import kotlinx.android.synthetic.main.loginactivity.*
//
//class LoginActivity :AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //setContentView(null)
//        setContentView(R.layout.loginactivity)
//
//
//        SignUp.setOnClickListener {
//            val userEmail = editID.text.toString()
//            val password = editpw.text.toString()
//            Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
//                .addOnCompleteListener(this){
//                    if(it.isSuccessful){
//                        startActivity(Intent(this, MainActivity::class.java))
//                    } else{
//                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }
//        SignIn.setOnClickListener {
//            val userEmail = editID.text.toString()
//            val password = editpw.text.toString()
//            Firebase.auth.signInWithEmailAndPassword(userEmail, password)
//                .addOnCompleteListener(this){
//                    if(it.isSuccessful){
//                        startActivity(Intent(this, MainActivity::class.java))
//                    } else{
//                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//        }
//
//
//
//    }
//}

