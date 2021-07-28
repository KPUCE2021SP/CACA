package com.example.myapplication

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.loginactivity.*

class LoginActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(null)
        setContentView(R.layout.loginactivity)


        SignUp.setOnClickListener {
            val userEmail = editID.text.toString()
            val password = editpw.text.toString()
            Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                    } else{
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        SignIn.setOnClickListener {
            val userEmail = editID.text.toString()
            val password = editpw.text.toString()
            Firebase.auth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                    } else{
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }

        }



    }
}