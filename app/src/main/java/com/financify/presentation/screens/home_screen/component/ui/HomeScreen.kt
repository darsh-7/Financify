package com.financify.presentation.screens.home_screen.component.ui

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.financify.R
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.presentation.navigation.Screens
import com.financify.presentation.screens.home_screen.viewmodel.HomeViewModel
import com.leinardi.android.speeddial.compose.FabWithLabel
import com.leinardi.android.speeddial.compose.SpeedDial
import com.leinardi.android.speeddial.compose.SpeedDialOverlay
import com.leinardi.android.speeddial.compose.SpeedDialState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddTransactionClicked: (TransactionType) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current

    val totalBalance by viewModel.totalBalance.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    val recentSavings by viewModel.recentSavings.collectAsState()
    val incomeSources by viewModel.incomeSources.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalOutcome by viewModel.totalOutcome.collectAsState()

    val itemsToShow = incomeSources.take(4)
    val savingsListLimited = recentSavings.take(4)

    val colors = listOf(
        Color(0xFF377CC8),
        Color(0xFFE0533D),
        Color(0xFFE78C9D),
        Color(0xFFFBC02D)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile image",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Good Morning",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                        Text(
                            text = "User Name",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                IconButton(onClick = {
                    Toast.makeText(context, "Notifications clicked", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(28.dp),
                        tint = Color.Black
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = rememberImagePainter(data = R.drawable.balancee),
                    contentDescription = "Background image showing balance",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.7f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Total Balance",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFF5F5F5)
                        )
                        Text(
                            text = "$${String.format("%,.0f", totalBalance)}",                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                onAddTransactionClicked(TransactionType.INCOME)
                            },
                            modifier = Modifier
                                .background(color = Color.White.copy(alpha = 0.9f), shape = CircleShape)
                                .padding(4.dp)
                                .size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase Balance",
                                tint = Color(0xFF377CC8)
                            )
                        }

                        IconButton(
                            onClick = {
                                onAddTransactionClicked(TransactionType.EXPENSE)
                            },
                            modifier = Modifier
                                .background(color = Color.White.copy(alpha = 0.9f), shape = CircleShape)
                                .padding(4.dp)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease Balance",
                                tint = Color(0xFFE0533D)
                            )
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .paint(
                        painterResource(id = R.drawable.stat2),
                        contentScale = ContentScale.Crop
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                onAddTransactionClicked(TransactionType.INCOME)
                            },
                            modifier = Modifier
                                .background(color = Color.White.copy(alpha = 0.9f), shape = CircleShape)
                                .padding(4.dp)
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Income",
                                tint = Color(0xFF377CC8)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Income",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$${String.format("%,.0f", totalIncome)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.padding(end = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                onAddTransactionClicked(TransactionType.EXPENSE)
                            },
                            modifier = Modifier
                                .background(color = Color.White.copy(alpha = 0.9f), shape = CircleShape)
                                .padding(4.dp)
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Add Outcome",
                                tint = Color(0xFFE0533D)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Outcome",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$${String.format("%,.0f", totalOutcome)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Earnings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            if (itemsToShow.isEmpty()) {
                Card(
                    modifier = Modifier
                        .width(160.dp)
                        .height(160.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Gray)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No\nEarnings Yet!",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    itemsIndexed(itemsToShow) { index, transaction ->
                        val cardColor = colors[index % colors.size]
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .height(160.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = transaction.title.first().toString(),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = cardColor
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = transaction.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "$${transaction.amount}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Savings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                TextButton(
                    onClick = { navController.navigate(Screens.SavingListScreen.route) }
                ) {
                    Text(
                        text = "See All",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF489FCD)
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(horizontal = 8.dp)
            ) {
                itemsIndexed(savingsListLimited) { index, item ->
                    val progressColor = colors[index % colors.size]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.goalName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )
                                IconButton(onClick = {
                                    Toast.makeText(context, "${item.goalName} options clicked", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Icon",
                                        tint = Color(0xFF1976D2)
                                    )
                                }
                            }
                            Text(
                                text = "$${item.targetAmount}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            LinearProgressIndicator(
                                progress = if (item.targetAmount != 0.0) (item.savedAmount.toFloat() / item.targetAmount.toFloat()) else 0f,
                                color = progressColor,
                                trackColor = Color.LightGray.copy(alpha = 0.3f),
                                strokeCap = StrokeCap.Round,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                        }
                    }
                }
            }

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transactions",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            TextButton(
                onClick = {
                    navController.navigate(Screens.TransactionsListScreen.passUserId("testId"))
                }
            ) {
                Text(
                    text = "See All",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF489FCD)
                )
            }
        }

            Spacer(modifier = Modifier.height(8.dp))
            val groupedTransactions = recentTransactions
                .groupBy {
                    Instant.ofEpochMilli(it.date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
                .toSortedMap(compareByDescending { it })

            fun formatDate(date: LocalDate): String {
                val today = LocalDate.now()
                val yesterday = today.minusDays(1)
                return when (date) {
                    today -> "Today"
                    yesterday -> "Yesterday"
                    else -> date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                }
            }


            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                groupedTransactions.forEach { (date, transactionsForDate) ->
                    Text(
                        text = formatDate(date),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    transactionsForDate.forEach { transaction ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            color = if (transaction.type == TransactionType.INCOME) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = transaction.title.first().toString(),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (transaction.type == TransactionType.INCOME) Color(0xFF2E7D32) else Color(0xFFC62828)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = transaction.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = transaction.description,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Gray
                                    )
                                }
                                val sign = if (transaction.type == TransactionType.INCOME) "+" else "-"
                                val amountWithSign = "$sign ${String.format("$%,.0f", kotlin.math.abs(transaction.amount))}"
                                Text(
                                    text = amountWithSign,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (transaction.type == TransactionType.INCOME) Color(0xFF2E7D32) else Color(0xFFC62828)
                                )
                            }
                        }
                    }
                }
            }}
        var speedDialState by rememberSaveable { mutableStateOf(SpeedDialState.Collapsed) }
        var overlayVisible: Boolean by rememberSaveable { mutableStateOf(speedDialState.isExpanded()) }

        if (overlayVisible) {
            SpeedDialOverlay(
                visible = true,
                modifier = Modifier
                    .matchParentSize()
                    .zIndex(0f),
                onClick = {
                    overlayVisible = false
                    speedDialState = speedDialState.toggle()
                }
            )
        }

        SpeedDial(
            fabClosedBackgroundColor = Color(0xFF377BC7),
            fabOpenedBackgroundColor = Color(0xFF377BC7),
            fabClosedContentColor = Color.White,
            fabOpenedContentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .zIndex(1f),
            state = speedDialState,
            onFabClick = { expanded ->
                overlayVisible = !expanded
                speedDialState = if (expanded) SpeedDialState.Collapsed else SpeedDialState.Expanded
            }
        ) {
            item {
                FabWithLabel(
                    onClick = { onAddTransactionClicked(TransactionType.EXPENSE) },
                    labelContent = { Text("Expense") }
                ) {
                    Icon(Icons.Default.KeyboardArrowDown, null)
                }
            }
            item {
                FabWithLabel(
                    onClick = { onAddTransactionClicked(TransactionType.INCOME) },
                    labelContent = { Text("Income") }
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, null)
                }
            }
        }
    }
}
