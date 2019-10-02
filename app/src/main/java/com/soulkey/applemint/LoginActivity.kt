package com.soulkey.applemint

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_acitivity.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_acitivity)

        val auth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            val inputEmail = et_email.text.toString()
            val inputPassword = et_password.text.toString()
            if(inputEmail.isNotEmpty() and inputPassword.isNotEmpty()){
                auth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener {
                    when(it.isSuccessful){
                        true-> {
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }
                        false-> Toast.makeText(applicationContext, "Login Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(applicationContext, "Please Input Email or Password..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
