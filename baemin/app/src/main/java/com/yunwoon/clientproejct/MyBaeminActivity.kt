package com.yunwoon.clientproejct

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import com.yunwoon.clientproejct.databinding.ActivityMyBaeminBinding
import com.yunwoon.clientproejct.sharedPreference.MyApplication
import com.yunwoon.clientproejct.sharedPreference.PreferenceUtil
import com.yunwoon.clientproejct.sqlite.DBHelper

class MyBaeminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyBaeminBinding
    private lateinit var dbHelper : DBHelper
    private var status : String = ""

    private val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBaeminBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 최초 실행시 데이터베이스 세팅
        dbHelper = DBHelper(this, "MemberDB", null, 1)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("생명주기", "My Baemin onRestart")

        MyApplication.prefs.remove("nickName")
        MyApplication.prefs.remove("password")
    }

    override fun onStart() {
        super.onStart()
        Log.d("생명주기", "My Baemin onStart")

        // 사용자 로그인 상태 가져옴
        // 읽기 모드로 데이터 저장소 가져옴
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select `status` from MemberDB where email='abc7017@gmail.com'", null)

        while(cursor.moveToNext()) {
            status = cursor.getString(0)
        }
        db.close()
    }

    override fun onResume() {
        super.onResume()
        Log.d("생명주기", "My Baemin onResume")

        val transaction = manager.beginTransaction()
        val fragmentMyBaeminFragment = MyBaeminFragment()
        val fragmentMyBaemin2Fragment = MyBaemin2Fragment()

        if(status == "N")
            transaction.replace(R.id.frameLayout, fragmentMyBaemin2Fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        else
            transaction.replace(R.id.frameLayout, fragmentMyBaeminFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    override fun onPause() {
        super.onPause()
        Log.d("생명주기", "My Baemin onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("생명주기", "My Baemin onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("생명주기", "My Baemin onDestroy")
    }
}
