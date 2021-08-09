package com.yunwoon.clientproejct

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import com.yunwoon.clientproejct.databinding.ActivityReviewBinding
import com.yunwoon.clientproejct.recyclerview.ReviewAdapter
import com.yunwoon.clientproejct.recyclerview.ReviewData
import com.yunwoon.clientproejct.recyclerview.SwipeHelper
import java.io.ByteArrayOutputStream

class ReviewActivity : AppCompatActivity() {

    private lateinit var reviewAdapter: ReviewAdapter
    private val reviewData = mutableListOf<ReviewData>()
    private lateinit var binding: ActivityReviewBinding

    private val REQUEST_CREATE_CODE = 101
    private val REQUEST_MODIFY_CODE = 102
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        initReviewRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // 옵션 메뉴 생성
        menuInflater.inflate(R.menu.reviewmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // 옵션 메뉴 각 아이템 클릭 시
        when (item.itemId) {
            R.id.menu_add -> {
                this.startActivityForResult(Intent(this,ReviewCreateActivity::class.java), REQUEST_CREATE_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getReviewCount() {
        binding.reviewCountTextView.text = reviewAdapter.itemCount.toString() // 리뷰 수 받아오기
    }

    private fun initReviewRecyclerView() { // 리사이클러뷰 시작
        reviewAdapter = ReviewAdapter(this)
        binding.reviewRecyclerView.adapter = reviewAdapter

        val resources : Resources = this.resources
        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.review_image)
        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.review_image2)
        val bitmap3 = BitmapFactory.decodeResource(resources, R.drawable.review_image3)
        val bitmap4 = BitmapFactory.decodeResource(resources, R.drawable.review_image4)

        reviewData.apply {
            add(ReviewData("크리스피크림도넛", "3주일 전", 5.0f, bitmap1, "도넛이 먹고 싶을 때 크리스피를 찾아요"))
            add(ReviewData("아우어베이커리 가로수길점", "1개월 전", 3.0f, bitmap2, "더티초코 초코가 넘쳐요 💘 좋아요"))
            add(ReviewData("구공탄곱창 연신내점", "3개월 전", 4.0f, bitmap3, "치즈 막창 맛집"))
            add(ReviewData("그믐족발 압구정로데오점", "6개월 전", 5.0f, bitmap4, "튀김 족발 맛집이네요"))

            reviewAdapter.reviewData = reviewData
        }

        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.reviewRecyclerView) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val buttons: List<UnderlayButton>
                val modifyButton = modifyButton(position)
                val deleteButton = deleteButton(position)
                buttons = listOf(deleteButton, modifyButton)

                return buttons
            }
        })

        // ItemTouchHelper를 제공된 RecyclerView에 연결
        itemTouchHelper.attachToRecyclerView(binding.reviewRecyclerView)
    }

    private fun modifyButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "수정",
            14.0f,
            R.color.black,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    moveToReviewModifyActivity(position)
                }
            })
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "삭제",
            14.0f,
            android.R.color.holo_red_dark,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    deleteReviewRecyclerView(position)
                }
            })
    }

    fun moveToReviewReadActivity(position: Int) {
        this.position = position
        val intent = Intent(this, ReviewReadActivity::class.java)

        val stream = ByteArrayOutputStream()
        val img = reviewData[position].reviewImage
        img.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        val byteArray = stream.toByteArray()

        intent.putExtra("storeName", reviewData[position].storeName)
        intent.putExtra("rating", reviewData[position].rating)
        intent.putExtra("reviewDate", reviewData[position].reviewDate)
        intent.putExtra("reviewContent", reviewData[position].reviewContent)
        intent.putExtra("reviewImage", byteArray)
        startActivity(intent)
    }

    fun moveToReviewModifyActivity(position: Int) {
        this.position = position
        val intent = Intent(this, ReviewModifyActivity::class.java)

        val stream = ByteArrayOutputStream()
        val img = reviewData[position].reviewImage
        img.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        val byteArray = stream.toByteArray()

        intent.putExtra("storeName", reviewData[position].storeName)
        intent.putExtra("rating", reviewData[position].rating)
        intent.putExtra("reviewContent", reviewData[position].reviewContent)
        intent.putExtra("reviewImage", byteArray)
        startActivityForResult(intent,REQUEST_MODIFY_CODE)
    }

    fun deleteReviewRecyclerView(position: Int) { // 리뷰 삭제
        reviewAdapter.deleteItem(position)
        getReviewCount()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CREATE_CODE) { // 리뷰 생성 Result 받아오기
            if(resultCode == RESULT_OK) {
                try {
                    Log.d("생명주기", "review activity activityResult")
                    val storeName = data!!.getStringExtra("storeName")
                    val reviewDate = data!!.getStringExtra("reviewDate")
                    val rating = data!!.getFloatExtra("rating", 0.0f)
                    val byteArray = data!!.getByteArrayExtra("reviewImage")
                    val reviewContent = data!!.getStringExtra("reviewContent")

                    val reviewBitmapImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                    reviewData.add(0, ReviewData(storeName!!, reviewDate!!, rating, reviewBitmapImage, reviewContent!!))
                    reviewAdapter.addToChangeStatus()
                } catch (e : Exception) { }
            }
            else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "리뷰 작성 취소", Toast.LENGTH_SHORT).show()
            }
        }

        if(requestCode == REQUEST_MODIFY_CODE) { // 리뷰 수정 Result 받아오기
            if(resultCode == RESULT_OK) {
                val rating = data!!.getFloatExtra("rating", 0.0f)
                val reviewContent = data!!.getStringExtra("reviewContent")
                val reviewImageByteArray = data!!.getByteArrayExtra("reviewImageByteArray")

                // byteArray -> bitmap
                val reviewImageBitmap = BitmapFactory.decodeByteArray(reviewImageByteArray, 0, reviewImageByteArray.size)

                reviewAdapter.modifyItem(position, rating, reviewContent, reviewImageBitmap)
            }
            else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "리뷰 수정 취소", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("생명주기", "review activity start")
        reviewAdapter.notifyDataSetChanged()
        getReviewCount()
    }
}