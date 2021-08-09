package com.yunwoon.clientproejct

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yunwoon.clientproejct.databinding.ActivityReviewcreateBinding
import java.io.ByteArrayOutputStream
import java.util.*

class ReviewCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewcreateBinding
    private val REQUEST_GALLERY_CODE = 1
    lateinit var img : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewcreateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        binding.reviewDateTextView.setOnClickListener {
            showDatePicker()
        }

        binding.reviewImageView.setOnClickListener {
            showGallery()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.completemenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_complete -> {
                passReviewData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()

        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, y, m, d->
            binding.reviewDateTextView.text = "$y-$m-$d"
           }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
    }

    private fun showGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
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
                } catch (e : Exception) { }
            }
            else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun passReviewData() {
        val intent = Intent()

        val stream = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        intent.putExtra("storeName", binding.storeNameEditView.text.toString())
        intent.putExtra("reviewDate", binding.reviewDateTextView.text.toString())
        intent.putExtra("rating", binding.reviewRatingBar.rating)
        intent.putExtra("reviewImage", byteArray)
        intent.putExtra("reviewContent", binding.reviewContentEditText.text.toString())

        setResult(RESULT_OK, intent)
        finish()
    }
}