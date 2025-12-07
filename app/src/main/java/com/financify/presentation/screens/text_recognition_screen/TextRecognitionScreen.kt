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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.presentation.screens.add_transaction.viewmodel.TransactionViewModel
import com.financify.presentation.screens.text_recognition_screen.components.CameraPreview

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun TextRecognitionScreen(navController: NavController, viewModel: TransactionViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    var isProcessing by remember { mutableStateOf(false) }


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
//        if (isGranted) {
//            // Permission granted, can start camera
//        } else {
//            // Permission denied, handle appropriately (e.g., show a message)
//        }
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

    LaunchedEffect(viewModel.isOcrLoading, viewModel.isParsing) {
        if (!viewModel.isOcrLoading && !viewModel.isParsing && isProcessing) {
            isProcessing = false
            navController.navigate("transaction/${TransactionType.EXPENSE.name}")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(controller = cameraController, modifier = Modifier.fillMaxSize())

        if (isProcessing || viewModel.isOcrLoading || viewModel.isParsing) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Analyzing Receipt...")
            }
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
                                isProcessing = true
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