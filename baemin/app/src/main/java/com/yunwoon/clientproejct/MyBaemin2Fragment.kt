package com.yunwoon.clientproejct

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.yunwoon.clientproejct.databinding.FragmentMyBaemin2Binding

class MyBaemin2Fragment : Fragment() {
    private var _binding: FragmentMyBaemin2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBaemin2Binding.inflate(inflater, container, false)
        val view = binding.root

        binding.linearLayout.setOnClickListener {
            this.startActivity(Intent(activity, LoginActivity::class.java))
        }

        binding.reviewImageView.setOnClickListener {
            Toast.makeText(context, "로그인이 필요한 서비스입니다.\n            로그인해 주세요.", Toast.LENGTH_SHORT)
                .apply { setGravity(Gravity.CENTER, 0, 0); show() }
            this.startActivity(Intent(activity, LoginActivity::class.java))
        }

        return view
    }
}
