package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_password.*

class Login_passwordActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_password)

        auth =FirebaseAuth.getInstance()

        rePassword_Btn.setOnClickListener {
            resetPassword(passwordSearch.text.toString())
        }


    }

    private fun resetPassword(email:String){
        auth?.sendPasswordResetEmail(email)
            ?.addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(applicationContext, "비밀번호 재설정 메일을 보냈습니다.", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(applicationContext, "비밀번호 재설정 메일 전송을 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}