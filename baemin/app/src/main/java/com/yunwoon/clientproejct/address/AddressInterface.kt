package com.yunwoon.clientproejct.address

import androidx.annotation.NonNull
import com.yunwoon.clientproejct.address.models.AddressResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressInterface {
    @NonNull
    @GET("search")
    fun getAddress(
        @Query("request") request : String,
        @Query("size") size : Int,
        @Query("query") query : String,
        @Query("type") type : String,
        @Query("category") category : String,
        @Query("format") format : String,
        @Query("key") key : String
    ) : Call<AddressResponse>
}