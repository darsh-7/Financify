package com.financify.di

import android.content.Context
import androidx.room.Room
import com.financify.data.data_sources.local.room.GithubTransactionDatabase
import com.financify.data.data_sources.local.room.RepoListDao
import com.financify.presentation.utils.Constants.Companion.DATABASE_TRANSACTION


object DatabaseModule {

    fun provideGithubRepositoriesDatabase(
        context: Context
    ):GithubTransactionDatabase {
        return Room.databaseBuilder(
            context,
            GithubTransactionDatabase::class.java,
            DATABASE_TRANSACTION
        ).build()
    }

    fun provideRepoListDao(
        githubRepositoriesDatabase: GithubTransactionDatabase
    ):RepoListDao {
        return githubRepositoriesDatabase.repoListDao()
    }
}