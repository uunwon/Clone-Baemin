package com.yunwoon.clientproejct.recyclerview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.yunwoon.clientproejct.R
import com.yunwoon.clientproejct.ReviewActivity
import com.yunwoon.clientproejct.ReviewModifyActivity
import com.yunwoon.clientproejct.dialog.DeleteReviewdialogFragment
import kotlinx.android.synthetic.main.item_review.view.*
import java.io.ByteArrayOutputStream

class ReviewAdapter(private val context: Context) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>(){

    var reviewData = mutableListOf<ReviewData>()
    private val checkBoxStatus = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = reviewData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviewData[position])

        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            val activity = context as ReviewActivity
            activity.moveToReviewReadActivity(position)
        }

        // 아이템 체크 박스 클릭 이벤트
        holder.itemView.reviewCheckBox.isChecked = checkBoxStatus[position]
        holder.itemView.reviewCheckBox.setOnClickListener {
            if(!holder.itemView.reviewCheckBox.isChecked)
                checkBoxStatus.put(position, false)
            else
                checkBoxStatus.put(position, true)
            notifyItemChanged(position)
        }

        // 아이템 수정 버튼 클릭 이벤트
        holder.itemView.reviewModifyButton.setOnClickListener {
            val activity = context as ReviewActivity
            activity.moveToReviewModifyActivity(position)
        }

        // 아이템 삭제 버튼 클릭 이벤트
        holder.itemView.reviewDeleteButton.setOnClickListener {
            showDeleteDialogFragment(position)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val storeName: TextView = itemView.findViewById(R.id.storeNameTextView)
        private val reviewDate: TextView = itemView.findViewById(R.id.reviewDateTextView)
        private val rating: RatingBar = itemView.findViewById(R.id.reviewRatingBar)
        private val reviewImage: ImageView = itemView.findViewById(R.id.reviewImageView)
        private val reviewContent: TextView = itemView.findViewById(R.id.reviewContentTextView)

        fun bind(item: ReviewData) {
            storeName.text = item.storeName
            reviewDate.text = item.reviewDate
            rating.rating = item.rating
            reviewImage.setImageBitmap(item.reviewImage)
            reviewContent.text = item.reviewContent
        }
    }

    fun modifyItem(position: Int, rating: Float, reviewContent: String, reviewImage: Bitmap) {
        if(reviewData.size != 0) {
            reviewData[position].rating = rating
            reviewData[position].reviewContent = reviewContent
            reviewData[position].reviewImage = reviewImage
        }
        else
            Toast.makeText(context, "$position 번째 아이템 수정되죠",Toast.LENGTH_SHORT).show()
    }

    fun deleteItem(position: Int) {
        if(reviewData.size != 0) {
            val activity = context as ReviewActivity
            deleteToChangeStatus(position)
            reviewData.removeAt(position)
            activity.getReviewCount()
        }
        else
            Toast.makeText(context, "$position 번째 아이템 삭제되죠",Toast.LENGTH_SHORT).show()
        notifyDataSetChanged()
    }

    fun showDeleteDialogFragment(position: Int) {
        val dialog = DeleteReviewdialogFragment()
        val manager = (context as AppCompatActivity).supportFragmentManager
        val bundle = Bundle(1)
        bundle.putInt("position", position)

        dialog.arguments = bundle
        dialog.show(manager, "DeleteReviewdialogFragment")
    }

    fun addToChangeStatus() {
        for (position in reviewData.size downTo 0) {
            if(checkBoxStatus.get(position)) {
                checkBoxStatus.put(position, false)
                checkBoxStatus.put(position + 1, true)
            }
        }
    }

    private fun deleteToChangeStatus(i: Int) {
        checkBoxStatus.put(i, false)

        for(position in i + 1 until reviewData.size) {
            if(checkBoxStatus.get(position)) {
                checkBoxStatus.put(position - 1 , true)
                checkBoxStatus.put(position, false)
            }
        }
    }
}