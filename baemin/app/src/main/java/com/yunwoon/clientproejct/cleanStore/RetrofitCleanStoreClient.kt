package com.yunwoon.clientproejct.cleanStore

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCleanStoreClient {
    val sRetrofit = initRetrofit()
    private const val CLEAN_STORE_URL
        = "http://211.237.50.150:7080/openapi/1019552958104bab6bb1c7a1bb3d203e91172f67609d3793da66b721b50de2bb/json/Grid_20200713000000000605_1/"

    private fun initRetrofit() :Retrofit =
        Retrofit.Builder()
            .baseUrl(CLEAN_STORE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}