package com.financify.data.data_sources.local.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.financify.data.data_sources.local.room.entities.SavingGoal
import com.financify.data.data_sources.local.room.entities.SavingGoalDao
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {

    private lateinit var db: AppDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var savingGoalDao: SavingGoalDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        transactionDao = db.TransactionDao()
        savingGoalDao = db.savingGoalDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    // ==========================================
    // Transaction Tests
    // ==========================================

    @Test
    fun insertAndGetTransaction() = runBlocking {
        val id = UUID.randomUUID().toString()
        val transaction = Transaction(
            id = id,
            title = "Test Transaction",
            amount = 100.0,
            type = TransactionType.INCOME,
            date = System.currentTimeMillis(),
            description = "Test Description"
        )
        transactionDao.insertTransaction(transaction)
        
        val loaded = transactionDao.getTransactionById(id)
        assertNotNull(loaded)
        assertEquals(transaction.title, loaded?.title)
        assertEquals(transaction.amount, loaded!!.amount, 0.0)
    }

    @Test
    fun updateTransaction() = runBlocking {
        val id = UUID.randomUUID().toString()
        val transaction = Transaction(
            id = id,
            title = "Original Title",
            amount = 100.0,
            date = System.currentTimeMillis(),
            description = "Original"
        )
        transactionDao.insertTransaction(transaction)

        val updatedTransaction = transaction.copy(title = "Updated Title", amount = 200.0)
        transactionDao.updateTransaction(updatedTransaction)

        val loaded = transactionDao.getTransactionById(id)
        assertNotNull(loaded)
        assertEquals("Updated Title", loaded?.title)
        assertEquals(200.0, loaded!!.amount, 0.0)
    }

    @Test
    fun deleteTransaction() = runBlocking {
        val id = UUID.randomUUID().toString()
        val transaction = Transaction(
            id = id,
            title = "Delete Me",
            amount = 50.0,
            date = System.currentTimeMillis(),
            description = "To delete"
        )
        transactionDao.insertTransaction(transaction)

        // Verify insertion
        assertNotNull(transactionDao.getTransactionById(id))

        // Delete
        transactionDao.deleteTransaction(id)

        // Verify deletion
        assertNull(transactionDao.getTransactionById(id))
    }

    @Test
    fun getAllTransactions() = runBlocking {
        val t1 = Transaction(title = "T1", amount = 50.0, date = System.currentTimeMillis(), description = "D1")
        val t2 = Transaction(title = "T2", amount = 20.0, type = TransactionType.EXPENSE, date = System.currentTimeMillis(), description = "D2")
        
        transactionDao.insertTransaction(t1)
        transactionDao.insertTransaction(t2)

        val transactions = transactionDao.getAllTransactions().first()
        assertEquals(2, transactions.size)
    }

    // ==========================================
    // SavingGoal Tests
    // ==========================================

    @Test
    fun insertAndGetSavingGoal() = runBlocking {
        val goalId = "goal_1"
        val goal = SavingGoal(
            id = goalId,
            userId = "user_1",
            goalName = "New Car",
            targetAmount = 20000.0,
            savedAmount = 5000.0,
            goalType = "Vehicle",
            selectedDate = System.currentTimeMillis(),
            note = "Save for a car",
            color = "#FFFFFF",
            icon = "car_icon"
        )
        savingGoalDao.insertGoal(goal)

        val loadedList = savingGoalDao.getAllGoals().first()
        assertEquals(1, loadedList.size)
        assertEquals("New Car", loadedList[0].goalName)

        val loadedById = savingGoalDao.getGoalById(goalId).first()
        assertNotNull(loadedById)
        assertEquals("New Car", loadedById?.goalName)
    }

    @Test
    fun updateSavingGoal() = runBlocking {
        val goalId = "goal_update"
        val goal = SavingGoal(
            id = goalId,
            userId = "user_1",
            goalName = "Vacation",
            targetAmount = 1000.0,
            savedAmount = 0.0,
            goalType = "Travel",
            selectedDate = System.currentTimeMillis(),
            note = "Trip",
            color = "#000000",
            icon = "plane"
        )
        savingGoalDao.insertGoal(goal)

        val updatedGoal = goal.copy(savedAmount = 500.0, note = "Trip to Japan")
        savingGoalDao.updateGoal(updatedGoal)

        val loadedById = savingGoalDao.getGoalById(goalId).first()
        assertEquals("Trip to Japan", loadedById?.note)
        assertEquals(500.0, loadedById?.savedAmount!!, 0.0)
    }

    @Test
    fun deleteSavingGoal() = runBlocking {
        val goalId = "goal_delete"
        val goal = SavingGoal(
            id = goalId,
            userId = "user_1",
            goalName = "Delete Me",
            targetAmount = 100.0,
            savedAmount = 0.0,
            goalType = "Test",
            selectedDate = System.currentTimeMillis(),
            note = "Test",
            color = "#000000",
            icon = "test"
        )
        savingGoalDao.insertGoal(goal)
        
        // Verify insertion by checking list size or getById
        assertNotNull(savingGoalDao.getGoalById(goalId).first())

        savingGoalDao.deleteGoal(goalId)

        val loadedById = savingGoalDao.getGoalById(goalId).first()
        assertNull(loadedById)
    }
}