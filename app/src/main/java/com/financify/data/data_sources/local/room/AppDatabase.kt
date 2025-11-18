package com.financify.data.data_sources.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.financify.data.data_sources.local.room.entities.SavingGoal
import com.financify.data.data_sources.local.room.entities.SavingGoalDao
import com.financify.data.data_sources.local.room.entities.Transaction

@Database(entities = [Transaction::class, SavingGoal::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TransactionDao(): TransactionDao
    abstract fun savingGoalDao(): SavingGoalDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}