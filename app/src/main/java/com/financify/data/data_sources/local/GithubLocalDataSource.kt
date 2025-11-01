package com.financify.data.data_sources.local

import com.financify.data.data_sources.local.data_store.DataStorePreference
import com.financify.data.data_sources.local.room.RepoListDao
import com.financify.data.data_sources.local.room.entities.Transaction


class GithubLocalDataSource (
    private val repoListDao: RepoListDao,
   private val dataStorePreference: DataStorePreference,
) {
//    suspend fun getTrendingList(): List<Transaction> {
//        return repoListDao.getTransactionList()
//    }
//
//    suspend fun insertRepos(repoList: List<Transaction>) {
//        repoListDao.insertTransactionList(repoList)
//    }

    suspend fun saveIsFirstTimeEnterApp(isFirstTime: Boolean) {
        dataStorePreference.saveIsFirstTimeEnterApp(isFirstTime)
    }

    suspend fun readIsFirstTimeEnterApp() = dataStorePreference.readIsFirstTimeEnterApp()

}