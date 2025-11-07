package com.financify.presentation.screens.text_recognition_screen.model

data class ParsedTransaction(
    val title: String? = null,
    val amount: Double? = null,
    val category: String? = null,
    val date: String? = null,
    val description: String? = null
)

data class TextRecognitionState(
    val isLoading: Boolean = false,
    val recognizedText: String? = null,
    val parsedTransaction: ParsedTransaction? = null,
    val isParsing: Boolean = false
)