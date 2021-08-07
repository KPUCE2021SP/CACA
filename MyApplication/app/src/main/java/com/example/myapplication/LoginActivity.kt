package com.example.myapplication

//import android.support.v7.app.AppCompatActivity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        val intent= Intent(this, StartActivity::class.java) // 3초 뜨는 화면
        startActivity(intent)


        super.onCreate(savedInstanceState) // 위에 3초 화면 종료되면 시작
        setContentView(R.layout.loginactivity)


        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.et_eamil)
        editTextPassword = findViewById(R.id.et_password)


        if(MySharedPreferences.getUserId(this).isNullOrBlank() // 자동 로그인
            || MySharedPreferences.getUserPass(this).isNullOrBlank()) {
            //btn_signIn.setOnClickListener(){
            if(editTextEmail!!.text.toString().isNullOrBlank() || editTextPassword!!.text.toString().isNullOrBlank()) {
                //Toast.makeText(this, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
            }
            else {
                MySharedPreferences.setUserId(this, editTextEmail!!.text.toString().toString())
                MySharedPreferences.setUserPass(this, editTextPassword!!.text.toString().toString())
                signIn()
            }
            //}
        }
        else { // SharedPreferences 안에 값이 저장되어 있을 때 -> MainActivity로 이동
            Toast.makeText(this, "${MySharedPreferences.getUserId(this)}님 자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



        btn_signUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
//            signUp()
        }
        btn_signIn.setOnClickListener(){
            if (editTextEmail!!.text.toString()
                    .isNullOrBlank() || editTextPassword!!.text.toString().isNullOrBlank()
            ) {
                Toast.makeText(this, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
            } else {
                MySharedPreferences.setUserId(this, editTextEmail!!.text.toString().toString())
                MySharedPreferences.setUserPass(this, editTextPassword!!.text.toString().toString())
                signIn()
            }
        }


    }


    object MySharedPreferences {
        private val MY_ACCOUNT : String = "account"

        fun setUserId(context: Context, input: String) {
            val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = prefs.edit()
            editor.putString("MY_ID", input)
            editor.commit()
        }

        fun getUserId(context: Context): String {
            val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
            return prefs.getString("MY_ID", "").toString()
        }

        fun setUserPass(context: Context, input: String) {
            val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = prefs.edit()
            editor.putString("MY_PASS", input)
            editor.commit()
        }

        fun getUserPass(context: Context): String {
            val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
            return prefs.getString("MY_PASS", "").toString()
        }

        fun clearUser(context: Context) {
            val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = prefs.edit()
            editor.clear()
            editor.commit()
        }

    }



//    private fun signUp() {
//        email = editTextEmail!!.text.toString()
//        password = editTextPassword!!.text.toString()
//        if (isValidEmail && isValidPasswd) {
//            createUser(email, password)
//        }
//    }

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

//    // 회원가입
//    private fun createUser(email: String, password: String) {
//        firebaseAuth!!.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this){
//                if (it.isSuccessful) {
//                    // 회원가입 성공
//                    Toast.makeText(
//                        this@LoginActivity,
//                        "회원가입 성공!",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    // 회원가입 실패
//                    Toast.makeText(
//                        this@LoginActivity,
//                        "회원가입 실패",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//    }

    // 로그인
    private fun loginUser(email: String, password: String) {
        firebaseAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) {
                if (it.isSuccessful) {

                    // 로그인 성공
                    Toast.makeText(
                        this@LoginActivity,
                        "로그인 성공!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(application, MainActivity::class.java)
                    startActivity(intent)


                } else {
                    // 로그인 실패
                    Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        // 비밀번호 정규식
        val PASSWORD_PATTERN =
            Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")
    }




//    override fun onNewToken(token: String) { // FCM
//        Log.d(TAG, "Refreshed token: $token")
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
//    }
}