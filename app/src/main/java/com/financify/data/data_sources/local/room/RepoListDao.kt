package com.financify.data.data_sources.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.financify.data.data_sources.local.room.entities.Transaction

@Dao
interface


RepoListDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertTransactionList(repoList: List<Transaction>)
//
//    @Query("SELECT * FROM transactions")
//    suspend fun getTransactionList(): List<Transaction>
}