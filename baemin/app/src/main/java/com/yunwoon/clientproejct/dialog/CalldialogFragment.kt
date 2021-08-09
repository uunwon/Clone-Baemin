package com.yunwoon.clientproejct.dialog

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yunwoon.clientproejct.R
import com.yunwoon.clientproejct.databinding.FragmentCalldialogBinding

class CalldialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCalldialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalldialogBinding.inflate(inflater, container, false)
        val view = binding.root

        val num = Uri.parse("tel:050375044007")
        val callIntent = Intent(Intent.ACTION_CALL, num)

        // layout background transperency
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.callButton.setOnClickListener { // 전화 - 암시적 인텐트 실행
            startActivity(callIntent)
        }
        binding.cancelButton.setOnClickListener { // dialog 닫기
            dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.DialogTheme)
    }
}