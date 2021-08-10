package com.yunwoon.clientproejct.cleanStore

import androidx.annotation.NonNull
import com.yunwoon.clientproejct.cleanStore.models.CleanStoreResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CleanStoreInterface {
    @NonNull
    @GET("1/5")
    fun getCleanStore(
        @Query("RELAX_SI_NM") RELAX_SI_NM : String,
        @Query("RELAX_SIDO_NM") RELAX_SIDO_NM : String
    ) : Call<CleanStoreResponse>
}