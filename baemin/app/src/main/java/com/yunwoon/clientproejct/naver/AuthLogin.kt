package com.yunwoon.clientproejct.naver

import android.content.Context
import com.nhn.android.naverlogin.OAuthLogin
import com.yunwoon.clientproejct.R

class AuthLogin {
    companion object {
        private lateinit var mOAuthLoginModule : OAuthLogin

        fun init(context: Context) : OAuthLogin {
            mOAuthLoginModule = OAuthLogin.getInstance()
            mOAuthLoginModule.init(
                context
                , "tNG54ik3vGODvxaqgfPs"
                , "UjXsdFNyy2"
                , "clone-baemin"
            )

            return mOAuthLoginModule
        }
    }
}