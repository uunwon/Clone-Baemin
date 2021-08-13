package com.yunwoon.clientproejct.cleanStore

import com.yunwoon.clientproejct.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCleanStoreClient {
    val sRetrofit = initRetrofit()
    private val CLEAN_STORE_URL
        = "http://211.237.50.150:7080/openapi/${BuildConfig.DATA_CLEAN_STORE_SECRET_KEY}/json/Grid_20200713000000000605_1/"

    private fun initRetrofit() :Retrofit =
        Retrofit.Builder()
            .baseUrl(CLEAN_STORE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}