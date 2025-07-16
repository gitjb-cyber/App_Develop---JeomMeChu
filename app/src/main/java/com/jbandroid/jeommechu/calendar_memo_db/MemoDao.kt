package com.jbandroid.jeommechu.calendar_memo_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo_table ORDER BY date DESC")
    fun getAllMemos(): Flow<List<Memo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memo: Memo)

    @Delete
    suspend fun deleteMemo(memo: Memo)
}