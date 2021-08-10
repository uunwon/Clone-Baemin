package com.yunwoon.clientproejct.center

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCenterClient {
    val sRetrofit = initRetrofit()
    private const val CENTER_URL = "https://api.odcloud.kr/api/15077586/v1/"

    private fun initRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl(CENTER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}