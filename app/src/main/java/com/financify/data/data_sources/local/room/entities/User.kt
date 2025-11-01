package com.financify.data.data_sources.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val uid: String, // Unique ID from your authentication provider (e.g., Firebase Auth)
    val fullName: String,
    //val email: String,
    //val createdAt: Long = System.currentTimeMillis(),
    val defaultCurrency: String = "USD" // Good for future-proofing
)