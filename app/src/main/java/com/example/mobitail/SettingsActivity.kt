package com.example.mobitail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    lateinit var back_btn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        back_btn = findViewById(R.id.Back_btn)
        back_btn.setOnClickListener {
            var intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
        }
    }
}