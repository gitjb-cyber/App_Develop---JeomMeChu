package eu.tutorials.jeommechu.calendar_memo_db
/*

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// 메모 DAO
@Dao
abstract class MemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Create
    abstract fun addMemo(memoEntity: Memo) // 메모 추가/수정

    @Query("SELECT * FROM 'memo-table' ORDER BY date ASC") // Read (날짜 순차적으로 정렬)
    abstract fun getAllMemos(): Flow<List<Memo>>

    @Update // Update
    abstract fun updateMemo(memo: Memo)

    @Delete // Delete
    abstract fun deleteMemo(memo: Memo)
}
*/
