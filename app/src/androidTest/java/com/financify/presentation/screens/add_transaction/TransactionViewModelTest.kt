package com.financify.presentation.screens.add_transaction

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.financify.data.data_sources.local.room.AppDatabase
import com.financify.data.data_sources.local.room.TransactionDao
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.data.repository.TransactionRepository
import com.financify.presentation.screens.add_transaction.viewmodel.TransactionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TransactionViewModelTest {

    private lateinit var db: AppDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var repository: TransactionRepository
    private lateinit var viewModel: TransactionViewModel

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        transactionDao = db.TransactionDao()
        
        // Mock repository with real DAO or custom implementation if needed
        // Here we use a real repository with in-memory DB
        repository = TransactionRepository(transactionDao)
        
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = TransactionViewModel(repository)
    }

    @After
    fun tearDown() {
        db.close()
        Dispatchers.resetMain()
    }

    @Test
    fun saveTransaction_updatesStateAndInsertsToDb() = runTest {
        // Arrange
        viewModel.title = "Test Item"
        viewModel.amount = "150.0"
        viewModel.type = TransactionType.EXPENSE
        viewModel.description = "Test Desc"
        viewModel.selectedDate = 1000L
        viewModel.isCategory = false

        // Act
        val id = viewModel.saveTransactionAndGetId()

        // Assert
        val savedTransaction = transactionDao.getTransactionById(id)
        assertEquals("Test Item", savedTransaction?.title)
        assertEquals(150.0, savedTransaction?.amount!!, 0.0)
        assertEquals(TransactionType.EXPENSE, savedTransaction?.type)
        assertEquals("Test Desc", savedTransaction?.description)
    }

    @Test
    fun clearForm_resetsViewModelState() {
        // Arrange
        viewModel.title = "Dirty Title"
        viewModel.amount = "999"
        viewModel.type = TransactionType.EXPENSE
        viewModel.description = "Dirty Desc"
        viewModel.isCategory = true
        viewModel.selectedDate = 2000L

        // Act
        viewModel.clearForm()

        // Assert
        assertTrue(viewModel.title.isEmpty())
        assertTrue(viewModel.amount.isEmpty())
        assertEquals(TransactionType.INCOME, viewModel.type) // default
        assertTrue(viewModel.description.isEmpty())
        assertEquals(false, viewModel.isCategory)
        assertEquals(null, viewModel.selectedDate)
    }

    @Test
    fun fillFormWithTransaction_updatesViewModelState() {
        // Arrange
        val transaction = Transaction(
            id = "123",
            title = "Existing Item",
            amount = 50.0,
            type = TransactionType.INCOME,
            date = 3000L,
            description = "Existing Desc",
            isCategory = true
        )

        // Act
        viewModel.fillFormWithTransaction(transaction)

        // Assert
        assertEquals("Existing Item", viewModel.title)
        assertEquals("50.0", viewModel.amount)
        assertEquals(TransactionType.INCOME, viewModel.type)
        assertEquals("Existing Desc", viewModel.description)
    }

    @Test
    fun onDateSelected_updatesSelectedDate() {
        // Act
        viewModel.onDateSelected(123456789L)

        // Assert
        assertEquals(123456789L, viewModel.selectedDate)
    }
}