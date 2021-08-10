package com.yunwoon.clientproejct

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.yunwoon.clientproejct.databinding.ActivityAddressBinding
import com.yunwoon.clientproejct.sharedPreference.MyApplication

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    private lateinit var addressText:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar : Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        // 주소 검색 화면으로 이동
        binding.addressSearchImageButton.setOnClickListener {
            val intent = Intent(this, SearchAddressActivity::class.java)
            addressText = binding.addressEditText.text.toString()

            if(addressText.isBlank()) {
                Toast.makeText(applicationContext, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                intent.putExtra("address", addressText)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        binding.roadTextView.text = MyApplication.prefs.getString("road", "서울시 마포구 월드컵북로6길 61 연남토마")
        binding.parcelTextView.text = MyApplication.prefs.getString("parcel", "연남동 568-26 연남토마")
    }
}