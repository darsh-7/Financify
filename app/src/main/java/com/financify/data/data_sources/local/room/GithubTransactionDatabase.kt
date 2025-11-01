package com.financify.data.data_sources.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.financify.data.data_sources.local.room.entities.SavingGoal
import com.financify.data.data_sources.local.room.entities.Transaction

@Database(
    entities = [SavingGoal::class, Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class GithubTransactionDatabase: RoomDatabase() {
    abstract fun repoListDao(): RepoListDao
}