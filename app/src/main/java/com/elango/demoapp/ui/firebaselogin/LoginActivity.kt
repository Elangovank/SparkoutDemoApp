package com.elango.demoapp.ui.firebaselogin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.elango.demoapp.R
import com.elango.demoapp.ui.home.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var spinner: Spinner
    lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinnerCountries)
        spinner.setAdapter(
            ArrayAdapter(
                this,
                R.layout.inflate_country_code,
                CountryData.countryNames
            )
        )
        editText = findViewById(R.id.editTextPhone)
        findViewById<View>(R.id.buttonContinue).setOnClickListener(View.OnClickListener {
            val code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()]
            val number = editText.getText().toString().trim { it <= ' ' }
            if (number.isEmpty() || number.length < 10) {
                editText.setError(getString(R.string.error_validate_number_required))
                editText.requestFocus()
                return@OnClickListener
            }
            val phoneNumber = "+$code$number"
            val intent = Intent(this@LoginActivity, VerifyPhoneActivity::class.java)
            intent.putExtra("phonenumber", phoneNumber)
            startActivity(intent)
        })

    }
}