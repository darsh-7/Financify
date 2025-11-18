package com.financify.data.data_sources.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: String): Transaction?

    @Query("SELECT * FROM transactions WHERE isCategory = 1 ORDER BY date DESC")
    fun getAllCategories(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE isCategory = 1 AND type = :categoryType ORDER BY date DESC")
    fun getCategoriesByType(categoryType: TransactionType): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getTransactions(): PagingSource<Int, Transaction>
}