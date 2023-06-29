package com.example.mobitail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var welcome: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcome = findViewById(R.id.welcomebtn)

        welcome.setOnClickListener {
            var intent = Intent(this, StartUpActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
        }

    }
}