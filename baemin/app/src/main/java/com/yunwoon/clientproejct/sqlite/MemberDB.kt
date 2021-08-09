package com.yunwoon.clientproejct.sqlite

import android.provider.BaseColumns

object MemberDB {
    object MemberEntry : BaseColumns {
        const val TABLE_NAME = "MemberDB"
        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_NICKNAME = "nickName"
        const val COLUMN_USER_STATUS = "status"
    }
}