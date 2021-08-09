package com.yunwoon.clientproejct

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.yunwoon.clientproejct.databinding.ActivityMyPageBinding
import com.yunwoon.clientproejct.dialog.ProfiledialogFragment
import com.yunwoon.clientproejct.sharedPreference.MyApplication
import com.yunwoon.clientproejct.sqlite.DBHelper
import com.yunwoon.clientproejct.sqlite.MemberDB

class MyPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPageBinding
    private lateinit var dbHelper : DBHelper
    private var status : String = ""
    private var nickName : String = ""
    private var password : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Log.d("생명주기", "MyPage onCreate")

        dbHelper = DBHelper(this, "MemberDB", null, 1)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow) // 뒤로가기 버튼 아이콘 커스텀

        binding.profileCircleImageView.setOnClickListener {
            val dialog = ProfiledialogFragment()
            dialog.show(supportFragmentManager, "ProfiledialogFragment")
        }

        binding.logoutTextView.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val db = dbHelper.writableDatabase

        status = "N"
        val values = ContentValues().apply {
            put(MemberDB.MemberEntry.COLUMN_USER_STATUS, status)
        }

        val selection = "${MemberDB.MemberEntry.COLUMN_USER_EMAIL} LIKE ?"
        val selectionArgs = arrayOf("abc7017@gmail.com")

        db.update(MemberDB.MemberEntry.TABLE_NAME, values, selection, selectionArgs)

        Log.d("데이터베이스", "사용자 로그인 상태는 $status 로 업데이트 됨")

        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.savemenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                saveData()
                Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveData() {
        val db = dbHelper.writableDatabase

        var changeNickName = binding.nickNameEditText.text.toString()
        var changePassword = binding.passwordEditText.text.toString()

        if(changeNickName == "")
            changeNickName = nickName

        if(changePassword == "")
            changePassword = password

        val values = ContentValues().apply {
            put(MemberDB.MemberEntry.COLUMN_USER_NICKNAME, changeNickName)
            put(MemberDB.MemberEntry.COLUMN_USER_PASSWORD, changePassword)
        }

        val selection = "${MemberDB.MemberEntry.COLUMN_USER_EMAIL} LIKE ?"
        val selectionArgs = arrayOf("abc7017@gmail.com")

        db.update(MemberDB.MemberEntry.TABLE_NAME, values, selection, selectionArgs)
    }

    override fun onStart() {
        super.onStart()
        Log.d("생명주기", "My page onStart")

        // 사용자 닉네임 가져옴
        // 읽기 모드로 데이터 저장소 가져옴
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select nickName,password from MemberDB where email='abc7017@gmail.com'", null)

        while(cursor.moveToNext()) {
            nickName = cursor.getString(0)
            password = cursor.getString(1)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("생명주기", "My page onResume")

        binding.nickNameEditText.setText(MyApplication.prefs.getString("nickName", nickName))
        binding.passwordEditText.setText(MyApplication.prefs.getString("password", ""))
    }

    override fun onPause() {
        super.onPause()
        Log.d("생명주기", "My page onPause")

        MyApplication.prefs.putString("nickName", binding.nickNameEditText.text.toString())
        MyApplication.prefs.putString("password", binding.passwordEditText.text.toString())
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("생명주기", "My page onRestart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("생명주기", "My page onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("생명주기", "My page onDestroy")
    }
}
