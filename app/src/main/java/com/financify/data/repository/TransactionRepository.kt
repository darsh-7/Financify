package com.financify.data.repository

import com.financify.data.data_sources.local.room.TransactionDao
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType

class TransactionRepository(private val dao: TransactionDao) {

    suspend fun insertTransaction(transaction: Transaction): Long =
        dao.insertTransaction(transaction)

    fun getAllTransactions() = dao.getAllTransactions()

    suspend fun getTransactionById(id: String): Transaction? = dao.getTransactionById(id)

    fun getAllCategories() = dao.getAllCategories()

    fun getCategoriesByType(type: TransactionType) = dao.getCategoriesByType(type)

}