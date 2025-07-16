package com.jbandroid.jeommechu.calendar_memo_db

import kotlinx.coroutines.flow.Flow

class MemoRepository(private val memoDao: MemoDao) {
    val allMemos: Flow<List<Memo>> = memoDao.getAllMemos()

    suspend fun insert(memo: Memo) {
        memoDao.insertMemo(memo)
    }

    suspend fun delete(memo: Memo) {
        memoDao.deleteMemo(memo)
    }
}