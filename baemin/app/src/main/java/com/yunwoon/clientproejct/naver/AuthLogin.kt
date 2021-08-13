package com.yunwoon.clientproejct.naver

import android.content.Context
import com.nhn.android.naverlogin.OAuthLogin
import com.yunwoon.clientproejct.BuildConfig
import com.yunwoon.clientproejct.R

class AuthLogin {
    companion object {
        private lateinit var mOAuthLoginModule : OAuthLogin

        fun init(context: Context) : OAuthLogin {
            mOAuthLoginModule = OAuthLogin.getInstance()
            mOAuthLoginModule.init(
                context
                , BuildConfig.NAVER_CLIENT_ID
                , BuildConfig.NAVER_CLIENT_SECRET_KEY
                , "clone-baemin"
            )

            return mOAuthLoginModule
        }
    }
}