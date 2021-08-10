package com.yunwoon.clientproejct.center

import androidx.annotation.NonNull
import com.yunwoon.clientproejct.center.models.CenterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CenterInterface {
    @NonNull
    @GET("centers")
    fun getCenter(
        @Query("page") page : Int,
        @Query("perPage") perPage : Int,
        @Query("serviceKey") serviceKey : String
    ) : Call<CenterResponse>
}