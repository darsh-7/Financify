package com.financify.presentation.screens.add_transaction

import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.financify.data.BuildConfig
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.data.repository.TransactionRepository
import com.financify.presentation.screens.text_recognition_screen.model.ParsedTransaction
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    var type by mutableStateOf(TransactionType.INCOME)
    var amount by mutableStateOf("")
    var title by mutableStateOf("")
    var selectedDate by mutableStateOf<Long?>(null)
    var description by mutableStateOf("")
    var isCategory by mutableStateOf(false)

    var isOcrLoading by mutableStateOf(false)
    var isParsing by mutableStateOf(false)

    val transactions: Flow<PagingData<Transaction>> =
        repository.getPaginatedTransactions().cachedIn(viewModelScope)

    // Get categories based on the selected type
    @OptIn(ExperimentalCoroutinesApi::class)
    val categories: StateFlow<List<Transaction>> = snapshotFlow { type }.flatMapLatest { type ->
        repository.getCategoriesByType(type)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Save transaction and return its ID
    suspend fun saveTransactionAndGetId(): String {
        val transaction = Transaction(
            type = type,
            amount = amount.toDoubleOrNull() ?: 0.0,
            title = title,
            date = selectedDate ?: System.currentTimeMillis(),
            description = description,
            isCategory = isCategory
        )
        repository.insertTransaction(transaction)
        return transaction.id
    }

    // Fill form with transaction data
    fun fillFormWithTransaction(transaction: Transaction) {
        type = transaction.type
        amount = transaction.amount.toString()
        title = transaction.title
        description = transaction.description
    }

    // Get transaction by ID
    suspend fun getTransactionById(id: String): Transaction? {
        return repository.getTransactionById(id)
    }

    // Handle date selection
    fun onDateSelected(timestamp: Long) {
        selectedDate = timestamp
    }

    // Format date for display
    fun formatDate(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val zoneId = ZoneId.systemDefault() // local timezone
        val localDateTime = LocalDateTime.ofInstant(instant, zoneId)
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy h:mm a")
        return localDateTime.format(formatter)
    }

    // Clear form fields
    fun clearForm() {
        type = TransactionType.INCOME
        amount = ""
        title = ""
        description = ""
        isCategory = false
        selectedDate = null
    }

    val incomeTransactions = repository.getTransactionsByType(TransactionType.INCOME)

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val generativeModel = GenerativeModel(
        modelName = "gemini-flash-lite-latest", // Corrected model name
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    /*
     *      ------ One Shared ViewModel for both Screens(Text Recognition and Transaction Screen) ------
     *                      so AddTransactionScreen Deal with single data source.
     */

    // Process image for OCR
    fun processImage(imageProxy: ImageProxy) {
        viewModelScope.launch {
            isOcrLoading = true
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        isOcrLoading = false
                        if (visionText.text.isNotBlank()) analyzeReceipt(visionText.text)
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        isOcrLoading = false
                    }
                    .addOnCompleteListener { imageProxy.close() }
            } else {
                imageProxy.close()
                isOcrLoading = false
            }
        }
    }

    // Analyze receipt text
    fun analyzeReceipt(text: String) {
        viewModelScope.launch {
            isParsing = true
            val prompt =
                """Analyze the following receipt text and extract the title, amount, category, date, and a brief description. Return the data in JSON format. For example: {"title": "Adobe Illustrator", "amount": 50.00, "category": "Software", "date": "2024-07-29", "description": "Monthly subscription"}. Here is the receipt text: $text"""
            try {
                val response = generativeModel.generateContent(prompt)
                val responseText = response.text
                if (responseText != null) {
                    val startIndex = responseText.indexOf('{')
                    val endIndex = responseText.lastIndexOf('}')
                    if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                        val jsonString = responseText.substring(startIndex, endIndex + 1)
                        try {
                            val parsedTransaction =
                                Gson().fromJson(jsonString, ParsedTransaction::class.java)

                            type = TransactionType.EXPENSE
                            amount = parsedTransaction.amount?.toString() ?: ""
                            title = parsedTransaction.category ?: ""
                            selectedDate =
                                parsedTransaction.date?.toLongOrNull() ?: System.currentTimeMillis()
                            description = parsedTransaction.description ?: ""

                            isParsing = false
                        } catch (e: JsonSyntaxException) {
                            e.printStackTrace()
                            isParsing = false
                        }
                    } else {
                        // No JSON object found in the response
                        isParsing = false
                    }
                } else {
                    isParsing = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isParsing = false
            }
        }
    }
}