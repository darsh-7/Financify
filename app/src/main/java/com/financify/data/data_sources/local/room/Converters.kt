package com.financify.data.data_sources.local.room

import androidx.room.TypeConverter
import com.financify.data.data_sources.local.room.entities.TransactionType

class Converters {

    // Converts the Kotlin Enum into a String for storage in the database
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }

    // Converts the stored String ("INCOME", "EXPENSE") back into the Kotlin Enum
    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return try {
            TransactionType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            TransactionType.INCOME
        }
    }
}