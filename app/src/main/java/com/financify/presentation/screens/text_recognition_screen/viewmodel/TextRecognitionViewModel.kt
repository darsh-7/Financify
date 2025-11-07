package com.financify.presentation.screens.text_recognition_screen.viewmodel

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TextRecognitionState(
    val isLoading: Boolean = false,
    val recognizedText: String? = null
)

@OptIn(ExperimentalGetImage::class)
class TextRecognitionViewModel : ViewModel() {

    private val _state = MutableStateFlow(TextRecognitionState())
    val state = _state.asStateFlow()

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

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

    fun onTextProcessed() {
        _state.value = _state.value.copy(recognizedText = null)
    }
}