package com.yunwoon.clientproejct

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.yunwoon.clientproejct.databinding.ActivityReviewmodifyBinding
import com.yunwoon.clientproejct.recyclerview.ReviewAdapter
import com.yunwoon.clientproejct.recyclerview.ReviewData
import com.yunwoon.clientproejct.sqlite.DBHelper
import java.io.ByteArrayOutputStream

class ReviewModifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewmodifyBinding
    private lateinit var dbHelper : DBHelper

    private val REQUEST_GALLERY_CODE = 101
    private var position = 0
    lateinit var img : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewmodifyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 최초 실행시 데이터베이스 세팅
        dbHelper = DBHelper(this, "MemberDB", null, 1)

        // 데이터 세팅
        setReviewModifyData()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        binding.reviewImageFrameLayout.setOnClickListener { // 이미지 버튼 클릭 시
            showGallery()
        }
    }

    private fun setReviewModifyData() { // 최초 데이터 받아오기
        val storeName = intent.getStringExtra("storeName")
        val rating = intent.getFloatExtra("rating", 0.0f)
        val byteArray = intent.getByteArrayExtra("reviewImage")
        val reviewContent = intent.getStringExtra("reviewContent")
        val reviewContentEditable = SpannableStringBuilder(reviewContent)
        val reviewBitmapImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        position = intent.getIntExtra("position", 0)

        // 받아온 데이터 리뷰 수정 액티비티에 띄어주기
        binding.storeNameTextView.text = storeName
        binding.reviewRatingBar.rating = rating
        binding.nickNameTextView.text = getNickName()
        binding.reviewContentEditText.text = reviewContentEditable
        binding.reviewImageView.setImageBitmap(reviewBitmapImage)
    }

    private fun showGallery() { // 갤러리 사진 가져오기
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // 옵션 메뉴 생성
        menuInflater.inflate(R.menu.completemenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // 옵션 메뉴 선택 시
        when (item.itemId) {
            R.id.menu_complete -> {
                // Toast.makeText(applicationContext, "리뷰 수정될거임", Toast.LENGTH_SHORT).show()
                modifyReviewRecyclerView()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getNickName() : String { // 닉네임 가져오기
        // 사용자 닉네임 가져옴
        // 읽기 모드로 데이터 저장소 가져옴
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select nickName from MemberDB where email='abc7017@gmail.com'", null)
        var nickName = ""

        while(cursor.moveToNext()) {
            nickName = cursor.getString(0).toString()
        }
        return nickName
    }

    private fun modifyReviewRecyclerView() { // 리뷰 수정하기
        val intent = Intent()

        // drawable -> bitmap image
        val reviewImageDrawable = binding.reviewImageView.drawable
        val reviewImageBitmap = (reviewImageDrawable as BitmapDrawable).bitmap
        // bitmap -> byteArray
        val stream = ByteArrayOutputStream()
        reviewImageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        val reviewImageByteArray = stream.toByteArray()

        intent.putExtra("rating", binding.reviewRatingBar.rating)
        intent.putExtra("reviewContent", binding.reviewContentEditText.text.toString())
        intent.putExtra("reviewImageByteArray", reviewImageByteArray)

        setResult(RESULT_OK, intent)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 갤러리에서 이미지 가져오기
        if(requestCode == REQUEST_GALLERY_CODE) {
            if(resultCode == RESULT_OK) {
                try {
                    val input = contentResolver.openInputStream(data!!.data!!)
                    img = BitmapFactory.decodeStream(input)
                    // drawable = BitmapDrawable(resources, img)
                    input!!.close()

                    binding.reviewImageView.setImageBitmap(img)
                    binding.deleteImageView.visibility = View.INVISIBLE
                } catch (e : Exception) { }
            }
            else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "리뷰 수정 취소", Toast.LENGTH_SHORT).show()
    }
}