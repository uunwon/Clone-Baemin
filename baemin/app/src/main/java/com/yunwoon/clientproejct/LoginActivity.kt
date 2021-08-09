package com.yunwoon.clientproejct

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.widget.Toast
import com.yunwoon.clientproejct.databinding.ActivityLoginBinding
import com.yunwoon.clientproejct.databinding.ActivityMyBaeminBinding
import com.yunwoon.clientproejct.sqlite.DBHelper
import com.yunwoon.clientproejct.sqlite.MemberDB

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper : DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dbHelper = DBHelper(this, "MemberDB", null, 1)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        // emailEditText passwordEditText loginButton
        binding.loginButton.setOnClickListener {
            val loginStatus = login(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())

            if(loginStatus)
                loginSuccess()
            else
                Toast.makeText(applicationContext, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.naverButton.setOnClickListener {
            Toast.makeText(applicationContext, "네아로 구현 #가보자고", Toast.LENGTH_SHORT).show()
            naverLogin()
        }
    }

    private fun login(email: String?, password: String?): Boolean {
        // 읽기 모드로 데이터 저장소 가져옴
        val db = dbHelper.readableDatabase

        // 리턴받고자 하는 컬럼의 array
        val projection = arrayOf(BaseColumns._ID)

        // where 조건
        val selection = "${MemberDB.MemberEntry.COLUMN_USER_EMAIL} = ? AND ${MemberDB.MemberEntry.COLUMN_USER_PASSWORD} = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(
            MemberDB.MemberEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null, // group by 조건
            null, // having 조건
            null // order by 조건
        )

        // 반환된 cursor 의 값이 있다면! 정보 있다는 거니까! 로그인 성공!
        return cursor.count > 0
    }

    private fun loginSuccess() {
        val db = dbHelper.writableDatabase

        val status = "Y"
        val values = ContentValues().apply {
            put(MemberDB.MemberEntry.COLUMN_USER_STATUS, status)
        }

        val selection = "${MemberDB.MemberEntry.COLUMN_USER_EMAIL} LIKE ?"
        val selectionArgs = arrayOf("abc7017@gmail.com")

        db.update(MemberDB.MemberEntry.TABLE_NAME, values, selection, selectionArgs)

        Log.d("데이터베이스", "사용자 로그인 상태는 $status 로 업데이트 됨")

        finish()
    }

    private fun naverLogin() {
        // 네이버 로그인 구현 메서드
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("생명주기", "Login onDestroy")
    }
}