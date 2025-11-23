package com.financify.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.financify.data.data_sources.local.room.TransactionDao
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dao: TransactionDao) {

    suspend fun insertTransaction(transaction: Transaction): Long =
        dao.insertTransaction(transaction)

    fun getAllTransactions() = dao.getAllTransactions()

    suspend fun getTransactionCount(): Int = dao.getTransactionCount()

    fun getPaginatedTransactions(): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getTransactions() }
        ).flow
    }

    fun getPaginatedTransactionsByType(type: TransactionType): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getTransactionsByTypePaging(type) }
        ).flow
    }

    fun getPaginatedTransactionsOldest(): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getTransactionsOldest() }
        ).flow
    }

    suspend fun getTransactionById(id: String): Transaction? = dao.getTransactionById(id)

    fun getTransactionsByType(type: TransactionType) = dao.getTransactionsByType(type)

    fun getAllCategories() = dao.getAllCategories()

    fun getCategoriesByType(type: TransactionType) = dao.getCategoriesByType(type)

    fun getTransactionsBetweenDates(startDate: Long, endDate: Long) =
        dao.getTransactionsBetweenDates(startDate, endDate)


}
