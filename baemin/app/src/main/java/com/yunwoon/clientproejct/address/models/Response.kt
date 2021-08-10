package com.yunwoon.clientproejct.address.models

data class Response(
    val page: Page,
    val record: Record,
    val result: Result,
    val service: Service,
    val status: String
)