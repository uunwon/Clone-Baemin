package com.yunwoon.clientproejct

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhn.android.naverlogin.OAuthLogin
import com.yunwoon.clientproejct.databinding.FragmentMyBaeminBinding
import com.yunwoon.clientproejct.naver.AuthLogin
import com.yunwoon.clientproejct.sharedPreference.MyApplication
import com.yunwoon.clientproejct.sqlite.DBHelper

class MyBaeminFragment : Fragment() {
    private var _binding: FragmentMyBaeminBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper : DBHelper

    private lateinit var mOAuthLoginModule : OAuthLogin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBaeminBinding.inflate(inflater, container, false)
        val view = binding.root

        dbHelper = DBHelper(context, "MemberDB", null, 1)
        mOAuthLoginModule = context?.let { AuthLogin.init(it) }!!

        binding.linearLayout.setOnClickListener {
            this.startActivity(Intent(activity, MyPageActivity::class.java))
        }

        if(mOAuthLoginModule.getState(context).toString() == "OK")
            binding.nickNameTextView.text = MyApplication.prefs.getString("name", "이름")
        else
            binding.nickNameTextView.text = getNickName()

        binding.reviewLinearLayout.setOnClickListener {
            this.startActivity(Intent(activity, ReviewActivity::class.java))
        }

        return view
    }

    private fun getNickName() : String {
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
}