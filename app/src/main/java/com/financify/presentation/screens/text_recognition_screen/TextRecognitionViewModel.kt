package com.financify.presentation.screens.text_recognition_screen

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financify.data.BuildConfig
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
class TextRecognitionViewModel : ViewModel() {

    private val _state = MutableStateFlow(TextRecognitionState())
    val state = _state.asStateFlow()

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun processImage(imageProxy: ImageProxy) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        if (visionText.text.isNotBlank()) {
                            _state.value = _state.value.copy(isLoading = false, recognizedText = visionText.text)
                        } else {
                            _state.value = _state.value.copy(isLoading = false, recognizedText = null)
                        }
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        _state.value = _state.value.copy(isLoading = false)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun analyzeReceipt(text: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isParsing = true)
            val prompt = """Analyze the following receipt text and extract the title, amount, category, date, and a brief description. Return the data in JSON format. For example: {"title": "Adobe Illustrator", "amount": 50.00, "category": "Software", "date": "2024-07-29", "description": "Monthly subscription"}. Here is the receipt text: $text"""
            try {
                val response = generativeModel.generateContent(prompt)
                val parsedJson = response.text?.replace("```json", "")?.replace("```", "")?.trim()
                if (parsedJson != null) {
                    val parsedTransaction = Gson().fromJson(parsedJson, ParsedTransaction::class.java)
                    _state.value = _state.value.copy(isParsing = false, parsedTransaction = parsedTransaction)
                } else {
                    _state.value = _state.value.copy(isParsing = false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = _state.value.copy(isParsing = false)
            }
        }
    }

    fun onTextProcessed() {
        _state.value = _state.value.copy(recognizedText = null, parsedTransaction = null)
    }
}