package com.yunwoon.clientproejct.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.yunwoon.clientproejct.databinding.FragmentProfiledialogBinding


class ProfiledialogFragment : DialogFragment() {
    private var _binding: FragmentProfiledialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfiledialogBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.cameraLinearLayout.setOnClickListener {
            dispatchTakePictureIntent()
        }
        binding.closeImageButton.setOnClickListener { // dialog 닫기
            dismiss()
        }
        binding.galleryLinaerLayout.setOnClickListener {
            Toast.makeText(context, "갤러리 선택", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun dispatchTakePictureIntent() {
        val packageManager = requireActivity().packageManager

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivity(takePictureIntent)
            }
        }
    }

}