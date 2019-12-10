package com.soulkey.applemint.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.soulkey.applemint.R
import com.soulkey.applemint.ui.main.MainActivity
import kotlinx.android.synthetic.main.login_acitivity.*
import kotlinx.android.synthetic.main.login_acitivity.btn_login
import kotlinx.android.synthetic.main.login_acitivity.et_email
import kotlinx.android.synthetic.main.login_acitivity.et_password
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_acitivity)

        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.login_acitivity_loading)

        val auth = FirebaseAuth.getInstance()
        loginViewModel.updateProcess.observe(this, Observer {
            tv_login_update_msg.text = it
        })

        loginViewModel.isUpdateComplete.observe(this, Observer {
            if (it) {
                Handler().postDelayed({
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }, 1000)
            }
        })

        btn_login.setOnClickListener {view->
            val inputEmail = et_email.text.toString()
            val inputPassword = et_password.text.toString()
            if(inputEmail.isNotEmpty() and inputPassword.isNotEmpty()){
                TransitionManager.beginDelayedTransition(activity_login)
                constraintSet.applyTo(activity_login)
                auth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener {
                    if (it.isSuccessful){
                        loginViewModel.updateProcess.value = "Wait for Update.."

                        loginViewModel.loginProcess(inputEmail)
                    } else {
                        Toast.makeText(applicationContext, "Login Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Please Input Email or Password..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
