package com.yunwoon.clientproejct

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yunwoon.clientproejct.databinding.ActivityDeliveryBinding
import com.yunwoon.clientproejct.sharedPreference.MyApplication

class DeliveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeliveryBinding
    private val adArray : Array<Int> = arrayOf(R.drawable.ad_01, R.drawable.ad_02,
        R.drawable.ad_03, R.drawable.ad_04)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow) // 뒤로가기 버튼 아이콘 커스텀

        binding.titleAddress.text = MyApplication.prefs.getString("road", "서울시 마포구 연남동 568-26 연남토마")

        binding.categoryImageView.setOnClickListener {
            this.startActivity(Intent(this, StoreActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("생명주기", "Delivery onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("생명주기", "Delivery onReStart")

        val imageViewId = Math.random() * adArray.size
        binding.adImageView.setImageResource(adArray[imageViewId.toInt()])
    }

    override fun onStop() {
        super.onStop()
        Log.d("생명주기", "Delivery onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.d("생명주기", "Delivery onPause")
    }
}