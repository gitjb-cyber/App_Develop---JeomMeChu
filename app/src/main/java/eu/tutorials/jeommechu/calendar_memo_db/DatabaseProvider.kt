package eu.tutorials.jeommechu.calendar_memo_db

import android.content.Context

/*

object DatabaseProvider {
    @Volatile private var INSTANCE: MemoDatabase? = null

    fun getDatabase(context: Context): MemoDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MemoDatabase::class.java,
                "memo_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
*/
