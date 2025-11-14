package com.financify.presentation.screens.receipt_screen

import com.financify.presentation.screens.add_transaction.TransactionViewModel
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.navigation.NavController
import com.financify.R
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.repository.TransactionRepository
import com.financify.presentation.navigation.Screens
import com.financify.presentation.screens.receipt_screen.components.NotchedTicketShape
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReceiptUi(
    viewModel: TransactionViewModel, transactionId: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    var transaction by remember { mutableStateOf<Transaction?>(null) }
    val context = LocalContext.current
    val receiptView = LocalView.current


    LaunchedEffect(transactionId) {
        transaction = viewModel.getTransactionById(transactionId)
    }

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
                    "${transaction?.title?.uppercase()} RECEIPT",
                    fontSize = 22.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(14.dp)
                        .basicMarquee()
                )
                DashedDivider()

                transaction?.let {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        ReceiptRow("Amount:", "$${it.amount}")
                        ReceiptRow("Transaction Type:", it.type.name)
                        ReceiptRow("Date:", viewModel.formatDate(it.date))
                        ReceiptRow("Description:", it.description)

                        DashedDivider(Modifier.padding(horizontal = 0.dp, vertical = 16.dp))

                        ReceiptRow("Total", "$${transaction?.amount}", true)

                        DashedDivider(Modifier.padding(horizontal = 0.dp))
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = { navController.popBackStack(Screens.HomeScreen.route,true,) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C9BF9))
            ) {
                Text("Home", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier.padding(horizontal = 16.dp),
    color: Color = Color.Gray,
    thickness: Dp = 1.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 8.dp
) {
    Canvas(
        modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        val dashLengthPx = dashLength.toPx()
        val gapLengthPx = gapLength.toPx()

        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = thickness.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashLengthPx, gapLengthPx),
                phase = 0f
            ),
            cap = StrokeCap.Butt
        )
    }
}

@Composable
fun ReceiptRow(label: String, value: String?, bold: Boolean = false) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif
        )
        if (value != null) {
            Text(
                value,
                fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif

            )
        }
    }
}