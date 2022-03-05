package com.elango.demoapp.ui.firebaselogin

import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.EditText
import android.os.Bundle
import com.elango.demoapp.R
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import android.content.Intent
import android.view.View
import com.elango.demoapp.ui.home.MainActivity
import android.widget.Toast
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.FirebaseException
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity : FragmentActivity() {
    private var verificationId: String? = null
    private var mAuth: FirebaseAuth? = null
    lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verifyphone)
        mAuth = FirebaseAuth.getInstance()
        editText = findViewById(R.id.editTextCode)
        val phonenumber = intent.getStringExtra("phonenumber")
        sendVerificationCode(phonenumber)
        findViewById<View>(R.id.buttonSignIn).setOnClickListener(View.OnClickListener {
            val code = editText.getText().toString().trim { it <= ' ' }
            if (code.isEmpty() || code.length < 6) {
                editText.setError("Enter code...")
                editText.requestFocus()
                return@OnClickListener
            }
            verifyCode(code)
        })
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@VerifyPhoneActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@VerifyPhoneActivity,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun sendVerificationCode(number: String?) {
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(number!!) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    editText!!.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@VerifyPhoneActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
}