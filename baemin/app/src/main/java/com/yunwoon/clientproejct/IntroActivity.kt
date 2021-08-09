package com.yunwoon.clientproejct

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val handler = Handler()
        handler.postDelayed( {
            val intent = Intent( this, MainActivity::class.java)
            startActivity(intent) },
            3000) // 3초 후 종료
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
