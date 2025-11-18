package com.financify.presentation.screens.receipt_screen

import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.navigation.NavController
import com.financify.R
import com.financify.presentation.screens.receipt_screen.components.NotchedTicketShape
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReceiptUiPrev(navController: NavController, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val receiptView = LocalView.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Receipt", fontSize = 32.sp, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {
                        try {
                            val bitmap = receiptView.drawToBitmap()
                            val file = File(context.cacheDir, "receipt.png")
                            FileOutputStream(file).use { out ->
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                            }

                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                file
                            )

                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "image/png"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    "Share Receipt"
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "Failed to share receipt", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }) {
                        Icon(
                            painterResource(id = R.drawable.ic_share),
                            contentDescription = "Share",
                            tint = Color(0xFF0079FD),
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 140.dp),
                shape = NotchedTicketShape(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

            ) {
                Text(
                    "Shopping RECEIPT",
                    fontSize = 22.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(14.dp)
                        .basicMarquee()
                )
                DashedDivider()

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    ReceiptRow("Amount:", "$2000")
                    ReceiptRow("Transaction Type:", "INCOME")
                    ReceiptRow("Date:", "1/11/2025 06:00 PM")
                    ReceiptRow("Description:", "it.description")

                    DashedDivider(Modifier.padding(horizontal = 0.dp, vertical = 16.dp))

                    ReceiptRow("Total", "$2000", true)

                    DashedDivider(Modifier.padding(horizontal = 0.dp))
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = { navController.navigate("repo_list_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C9BF9))
            ) {
                Text("Home", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Preview
@Composable
private fun ReceiptUiPreview() {
    ReceiptUiPrev(navController = NavController(LocalContext.current))
}