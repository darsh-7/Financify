package com.financify.presentation.screens.text_recognition_screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.financify.presentation.navigation.Screens
import com.financify.presentation.screens.text_recognition_screen.components.CameraPreview
import com.financify.presentation.screens.text_recognition_screen.viewmodel.TextRecognitionViewModel

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun TextRecognitionScreen(navController: NavController, viewModel: TextRecognitionViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val state by viewModel.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, can start camera
        } else {
            // Permission denied, handle appropriately (e.g., show a message)
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(state.recognizedText) {
        if (state.recognizedText != null) {
            navController.navigate(Screens.TextRecognitionResultScreen.passText(state.recognizedText!!))
            viewModel.onTextProcessed()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(controller = cameraController, modifier = Modifier.fillMaxSize())

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                onClick = {
                    val executor = ContextCompat.getMainExecutor(context)
                    cameraController.takePicture(
                        executor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                viewModel.processImage(imageProxy)
                            }

                            override fun onError(exception: ImageCaptureException) {
                                super.onError(exception)
                                exception.printStackTrace()
                            }
                        }
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Take picture"
                )
            }
        }
    }

    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    cameraController.bindToLifecycle(lifecycleOwner)
}