//package com.financify.presentation.screens.text_recognition_screen.viewmodel
//
//import androidx.camera.core.ImageProxy
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.financify.data.BuildConfig
//import com.financify.presentation.screens.text_recognition_screen.model.ParsedTransaction
//import com.financify.presentation.screens.text_recognition_screen.model.TextRecognitionState
//import com.google.ai.client.generativeai.GenerativeModel
//import com.google.gson.Gson
//import com.google.gson.JsonSyntaxException
//import com.google.mlkit.vision.common.InputImage
//import com.google.mlkit.vision.text.TextRecognition
//import com.google.mlkit.vision.text.latin.TextRecognizerOptions
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
//class TextRecognitionViewModel : ViewModel() {
//
//    private val _state = MutableStateFlow(TextRecognitionState())
//    val state = _state.asStateFlow()
//
//    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//    private val generativeModel = GenerativeModel(
//        modelName = "gemini-flash-lite-latest", // Corrected model name
//        apiKey = BuildConfig.GEMINI_API_KEY
//    )
//
//    fun processImage(imageProxy: ImageProxy) {
//        viewModelScope.launch {
//            _state.value = _state.value.copy(isLoading = true)
//            val mediaImage = imageProxy.image
//            if (mediaImage != null) {
//                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
//                recognizer.process(image)
//                    .addOnSuccessListener { visionText ->
//                        if (visionText.text.isNotBlank()) {
//                            _state.value = _state.value.copy(isLoading = false, recognizedText = visionText.text)
//                        } else {
//                            _state.value = _state.value.copy(isLoading = false, recognizedText = null)
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        e.printStackTrace()
//                        _state.value = _state.value.copy(isLoading = false)
//                    }
//                    .addOnCompleteListener {
//                        imageProxy.close()
//                    }
//            } else {
//                imageProxy.close()
//                _state.value = _state.value.copy(isLoading = false)
//            }
//        }
//    }
//
//    fun analyzeReceipt(text: String) {
//        viewModelScope.launch {
//            _state.value = _state.value.copy(isParsing = true)
//            val prompt = """Analyze the following receipt text and extract the title, amount, category, date, and a brief description. Return the data in JSON format. For example: {"title": "Adobe Illustrator", "amount": 50.00, "category": "Software", "date": "2024-07-29", "description": "Monthly subscription"}. Here is the receipt text: $text"""
//            try {
//                val response = generativeModel.generateContent(prompt)
//                val responseText = response.text
//                if (responseText != null) {
//                    val startIndex = responseText.indexOf('{')
//                    val endIndex = responseText.lastIndexOf('}')
//                    if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
//                        val jsonString = responseText.substring(startIndex, endIndex + 1)
//                        try {
//                            val parsedTransaction = Gson().fromJson(jsonString, ParsedTransaction::class.java)
//                            _state.value = _state.value.copy(isParsing = false, parsedTransaction = parsedTransaction)
//                        } catch (e: JsonSyntaxException) {
//                            e.printStackTrace()
//                            _state.value = _state.value.copy(isParsing = false)
//                        }
//                    } else {
//                        // No JSON object found in the response
//                        _state.value = _state.value.copy(isParsing = false)
//                    }
//                } else {
//                    _state.value = _state.value.copy(isParsing = false)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _state.value = _state.value.copy(isParsing = false)
//            }
//        }
//    }
//
//    fun onTextProcessed() {
//        _state.value = _state.value.copy(recognizedText = null, parsedTransaction = null)
//    }
//}