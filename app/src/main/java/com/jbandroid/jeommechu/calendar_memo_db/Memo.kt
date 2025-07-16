package com.jbandroid.jeommechu.calendar_memo_db

import androidx.room.Entity
import androidx.room.PrimaryKey

// 날짜별 메모 저장 구조
@Entity(tableName = "memo_table")
data class Memo(
    @PrimaryKey
    val date: String,
    val memo: String = ""
)