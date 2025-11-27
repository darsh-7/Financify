package com.financify.presentation.screens.transaction_screen.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.data.repository.TransactionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow<TransactionFilter>(TransactionFilter.All)
    val selectedFilter: StateFlow<TransactionFilter> = _selectedFilter.asStateFlow()

    val paginatedTransactions: Flow<PagingData<Transaction>> = _selectedFilter.flatMapLatest { filter ->
        flow {
            val count = repository.getTransactionCount()
            emit(count > 0)
        }.flatMapLatest { hasData ->
            when (filter) {
                is TransactionFilter.All, is TransactionFilter.Recent -> repository.getPaginatedTransactions()
                is TransactionFilter.Income -> repository.getPaginatedTransactionsByType(TransactionType.INCOME)
                is TransactionFilter.Expenses -> repository.getPaginatedTransactionsByType(TransactionType.EXPENSE)
                is TransactionFilter.Oldest -> repository.getPaginatedTransactionsOldest()
            }
        }
    }.cachedIn(viewModelScope)

    private val _selectedTransaction = MutableStateFlow<Transaction?>(null)
    val selectedTransaction: StateFlow<Transaction?> = _selectedTransaction.asStateFlow()

    fun setFilter(filter: TransactionFilter) {
        _selectedFilter.value = filter
    }

    fun getTransactionById(id: String) {
        viewModelScope.launch {
            val transaction = repository.getTransactionById(id)
            if (transaction != null) {
                _selectedTransaction.value = transaction
            } else {
                if (id.startsWith("dummy-")) {
                     val idNum = id.removePrefix("dummy-").toIntOrNull() ?: 0
                     val isIncome = idNum % 2 == 0
                     _selectedTransaction.value = Transaction(
                        id = id,
                        title = "Transaction #$idNum",
                        amount = if (isIncome) 150.0 * idNum else -50.0 * idNum,
                        type = if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
                        date = System.currentTimeMillis() - (idNum * 3600000L),
                        description = "This is a description for transaction #$idNum."
                    )
                }
            }
        }
    }

    fun clearSelectedTransaction() {
        _selectedTransaction.value = null
    }
}

class DummyPagingSource(
    private val filter: TransactionFilter
) : PagingSource<Int, Transaction>() {
    override fun getRefreshKey(state: PagingState<Int, Transaction>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> {
        val page = params.key ?: 1
        return try {
            delay(1000)

            val allItems = (1..params.loadSize).map { i ->
                val id = (page - 1) * params.loadSize + i
                val isIncome = i % 2 == 0
                Transaction(
                    id = "dummy-$id",
                    title = "Transaction #$id",
                    amount = if (isIncome) 150.0 * i else -50.0 * i,
                    type = if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
                    date = System.currentTimeMillis() - (id * 3600000L),
                    description = "This is a description for transaction #$id."
                )
            }

            val filteredItems = when (filter) {
                is TransactionFilter.All -> allItems
                is TransactionFilter.Income -> allItems.filter { it.type == TransactionType.INCOME }
                is TransactionFilter.Expenses -> allItems.filter { it.type == TransactionType.EXPENSE }
                is TransactionFilter.Recent -> allItems.sortedByDescending { it.date }
                is TransactionFilter.Oldest -> allItems.sortedBy { it.date }
            }

            LoadResult.Page(
                data = filteredItems,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < 10) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
