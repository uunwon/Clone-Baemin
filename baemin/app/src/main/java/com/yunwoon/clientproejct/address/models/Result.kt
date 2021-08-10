package com.yunwoon.clientproejct.address.models

data class Result(
    val crs: String,
    val items: List<Item>,
    val type: String
)