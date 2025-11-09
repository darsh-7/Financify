package com.financify.data.data_sources.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.financify.data.data_sources.local.room.entities.SavingGoal
import com.financify.data.data_sources.local.room.entities.SavingGoalDao
import com.financify.data.data_sources.local.room.entities.Transaction

//@Database(
//    entities = [SavingGoal::class, Transaction::class],
//    version = 1,
//    exportSchema = false
//)
//abstract class GithubTransactionDatabase: RoomDatabase() {
//    abstract fun repoListDao(): RepoListDao
//}


@Database(
    entities = [SavingGoal::class],
    version =2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savingGoalDao(): SavingGoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "financify_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


