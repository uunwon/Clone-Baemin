package com.yunwoon.clientproejct

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Menu
import com.yunwoon.clientproejct.databinding.ActivityReviewreadBinding

class ReviewReadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewreadBinding
    private var position = 0
    lateinit var img : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewreadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        // 데이터 세팅
        setReviewReadData()
    }

    // 옵션 메뉴 생성 .. 기능은 없음
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.completemenu, menu)
        return true
    }

    // 최초 데이터 받아오기
    private fun setReviewReadData() {
        val storeName = intent.getStringExtra("storeName")
        val rating = intent.getFloatExtra("rating", 0.0f)
        val byteArray = intent.getByteArrayExtra("reviewImage")
        val reviewDate = intent.getStringExtra("reviewDate")
        val reviewContent = intent.getStringExtra("reviewContent")
        val reviewBitmapImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        position = intent.getIntExtra("position", 0)

        // 받아온 데이터 리뷰 수정 액티비티에 띄어주기
        binding.storeNameTextView.text = storeName
        binding.reviewRatingBar.rating = rating
        binding.reviewDateTextView.text = reviewDate
        binding.reviewContentTextView.text = reviewContent
        binding.reviewImageView.setImageBitmap(reviewBitmapImage)
    }
}