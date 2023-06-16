package com.example.mobitail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartUpActivity : AppCompatActivity() {
    lateinit var store_ac_btn: Button
    lateinit var customer_ac_btn: Button
    lateinit var login_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        store_ac_btn = findViewById(R.id.store_ac_btn)
        customer_ac_btn = findViewById(R.id.customer_ac_button)
        login_btn = findViewById(R.id.login_btn)

        store_ac_btn.setOnClickListener {

        }

        customer_ac_btn.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
        }

        login_btn.setOnClickListener {
            var intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
        }
    }
}