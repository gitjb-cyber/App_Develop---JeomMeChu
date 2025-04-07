package eu.tutorials.jeommechu.calendar_memo_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
// 메모 DAO
/*
@Dao
abstract class MemoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // Create
    abstract fun addMemo(memo: MemoEntity) // 메모 추가/수정

    @Query("SELECT * FROM 'memo-table'") // Read
    abstract fun getAllMemos(): Flow<List<MemoEntity>>

    @Update // Update
    abstract fun updateAMemo(memo: MemoEntity)

    @Delete // Delete
    abstract fun deleteAMemo(memo: MemoEntity)

    @Query("SELECT * FROM 'memo-table' WHERE date = :date")
    abstract fun getMemoByDate(date: String): Flow<MemoEntity?>
}

*/
