package com.financify.data.data_sources.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Enum to define the type of transaction
enum class TransactionType {
    INCOME,
    EXPENSE
}

//UUID.randomUUID().toString()
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey val id: String,
    //!val userId: String, // Foreign key to link to the User
    val title: String, // e.g., "Adobe Illustrator", "Paypal", "Sony Camera"
    val amount: Double,
    val type: TransactionType, // INCOME or EXPENSE
    val category: String, // e.g., "Subscription", "Salary", "Shopping"
    val date: Long, // Use Unix timestamp for easy sorting and querying data -> yyyy-MM-dd HH:mm:ss || 8712368712638172
    //!val accountId: String, // Foreign key to the Account/Wallet used
    val description: String? = null, // Optional description
    val receiptImageUrl: String? = null // optional store image that made the transaction using the OCR bill scan feature
)