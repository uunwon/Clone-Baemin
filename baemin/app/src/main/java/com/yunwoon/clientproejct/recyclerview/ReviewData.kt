package com.yunwoon.clientproejct.recyclerview

import android.graphics.Bitmap

data class ReviewData(
    val storeName: String,
    val reviewDate: String,
    var rating: Float,
    var reviewImage: Bitmap,
    var reviewContent: String
)