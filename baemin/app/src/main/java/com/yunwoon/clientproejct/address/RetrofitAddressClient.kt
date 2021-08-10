package com.yunwoon.clientproejct.address

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAddressClient {
    val sRetrofit = initRetrofit()
    private const val ADDRESS_URL = "http://api.vworld.kr/req/"

    private fun initRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(ADDRESS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}