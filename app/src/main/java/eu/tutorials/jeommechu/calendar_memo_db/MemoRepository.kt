package eu.tutorials.jeommechu.calendar_memo_db

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.Date
/*

class MemoRepository(private val memoDao: MemoDao) {

    suspend fun addAMemo(memoEntity: MemoEntity){
        memoDao.addMemo(memoEntity)
    }

    fun getMemo() : Flow<List<MemoEntity>> = memoDao.getAllMemos()

    fun getAWishByDate(date: String) : Flow<MemoEntity?> {
        return memoDao.getMemoByDate(date)
    }

    suspend fun updateAMemo(memoEntity: MemoEntity){
        memoDao.updateAMemo(memoEntity)
    }

    suspend fun deleteAMemo(memoEntity: MemoEntity){
        memoDao.deleteAMemo(memoEntity)
    }
}

*/
