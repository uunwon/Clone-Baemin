package com.yunwoon.clientproejct.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.yunwoon.clientproejct.ReviewActivity
import com.yunwoon.clientproejct.databinding.FragmentDeletereviewdialogBinding

class DeleteReviewdialogFragment : DialogFragment() {
    private var _binding: FragmentDeletereviewdialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeletereviewdialogBinding.inflate(inflater, container, false)
        val view = binding.root
        val position = arguments?.getInt("position")

        // layout background transperency
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.okTextView.setOnClickListener {
            (activity as ReviewActivity).deleteReviewRecyclerView(position!!)
            dismiss()
        }
        binding.closeTextView.setOnClickListener { // dialog 닫기
            Toast.makeText(context, "리뷰 삭제 취소", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return view
    }
}