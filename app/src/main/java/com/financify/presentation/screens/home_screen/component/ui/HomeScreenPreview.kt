package com.financify.presentation.screens.home_screen.component.ui

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberImagePainter
import com.financify.R
import com.financify.presentation.screens.home_screen.model.SavingItem
import com.financify.presentation.screens.home_screen.model.Transaction
import com.leinardi.android.speeddial.compose.FabWithLabel
import com.leinardi.android.speeddial.compose.SpeedDial
import com.leinardi.android.speeddial.compose.SpeedDialOverlay
import com.leinardi.android.speeddial.compose.SpeedDialState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalMaterialApi::class
)
@Composable
fun HomeScreenPrev() {
    val context = LocalContext.current
    val transactionsList = listOf(
        Transaction("1", "Salary", "fixed salary", 1200.0, LocalDate.now(), true),
        Transaction("2", "Rent Payment", "Rent Payment", -1200.0, LocalDate.now(), false),
        Transaction(
            "3",
            "Freelance Project",
            "Freelance Project",
            500.0,
            LocalDate.now().minusDays(1),
            true
        ),
        Transaction(
            "4",
            "Grocery Shopping",
            "shopping fee",
            -150.50,
            LocalDate.now().minusDays(1),
            false
        ),
        Transaction(
            "5",
            "Investment Return",
            "Investment Return",
            800.0,
            LocalDate.now().minusDays(2),
            true
        ),
        Transaction("6", "Coffee", "shopping fee", -5.0, LocalDate.now().minusDays(3), false)
    )

    val recentTransactions = transactionsList
        .sortedByDescending { it.date }
        .take(4)
    val groupedTransactions = recentTransactions
        .groupBy { it.date }
        .toSortedMap(compareByDescending { it })

    fun formatDate(date: LocalDate): String {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        return when (date) {
            today -> "Today"
            yesterday -> "Yesterday"
            else -> date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) // e.g., "Oct 26, 2023"
        }
    }

    val incomeTransactions = listOf(
        SavingItem("First Job Salary", "2000", 0.3f),
        SavingItem("Upwork", "1200", 0.6f),
        SavingItem("Second Job Salary", "1500", 0.5f),
        SavingItem("Bonus", "12000", 0.6f),
        SavingItem("Khamsat", "1000", 0.5f)
    )

//    val incomeTransactions by viewModel.incomeTransactions.collectAsState(initial = emptyList())
    val itemsToShow = incomeTransactions.take(4)

    val savingsList = listOf(
        SavingItem("MacBook", "$2000", 0.3f),
        SavingItem("iPhone", "$1200", 0.6f),
        SavingItem("Vacation", "$1500", 0.5f),
        SavingItem("house", "$12000", 0.6f),
        SavingItem("dress", "$1000", 0.5f)
    )
    val savingsListLimited = savingsList.take(4)

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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFF5F5F5)
                        )
                        Text(
                            text = "$25,000.40",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Wallet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "My Wallet Icon",
                            tint = Color.Black,
                            modifier = Modifier
                                .background(color = Color.White, shape = CircleShape)
                                .padding(4.dp)
                                .size(28.dp)
                                .clickable {
                                    Toast.makeText(context, "My Wallet clicked", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        )
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
                        Image(
                            painter = painterResource(id = R.drawable.ic_income),
                            contentDescription = "Income Icon",
                            modifier = Modifier.size(32.dp)
                        )

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
                                text = "$20.000",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.padding(end = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_outcome),
                            contentDescription = "Outcome Icon",
                            modifier = Modifier.size(32.dp)
                        )

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
                                text = "$17.000",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
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
                    text = "Earnings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                TextButton(
                    onClick = {
                        Toast.makeText(context, "See All clicked", Toast.LENGTH_SHORT).show()
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
                                        text = transaction.name.first().toString(),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = cardColor
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = transaction.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "$${transaction.totalAmount}",
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
                    onClick = {
//                    navController.navigate(Screens.SavingListScreen.route)
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
                                    text = item.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )

                                IconButton(onClick = {
                                    Toast.makeText(
                                        context,
                                        "${item.name} options clicked",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Icon",
                                        tint = Color(0xFF1976D2)
                                    )
                                }
                            }

                            Text(
                                text = item.totalAmount,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )

                            LinearProgressIndicator(
                                progress = item.savedPercent,
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
//                    navController.navigate(Screens.TransactionListScreen.route)
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
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                            modifier = Modifier
                                .fillMaxWidth(),
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
                                            color = if (transaction.isIncome) Color(0xFFE8F5E9) else Color(
                                                0xFFFFEBEE
                                            ),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = transaction.name.first().toString(),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (transaction.isIncome) Color(0xFF2E7D32) else Color(
                                            0xFFC62828
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = transaction.name,
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
                                val sign = if (transaction.isIncome) "+" else "-"
                                val amountWithSign = "$sign ${
                                    String.format(
                                        "$%,.2f",
                                        kotlin.math.abs(transaction.amount)
                                    )
                                }"
                                Text(
                                    text = amountWithSign,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (transaction.isIncome) Color(0xFF2E7D32) else Color(
                                        0xFFC62828
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
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
            fabClosedContentColor = Color(0xFFFFFFFF),
            fabOpenedContentColor = Color(0xFFFFFFFF),
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
                    fabBackgroundColor = Color(0xFFEFB0B0),
                    fabContentColor = Color(0xFFFFFFFF),
                    labelBackgroundColor = Color(0xFFFFFFFF),
                    onClick = { },
                    labelContent = {
                        Text(
                            text = "Expense", color = Color(0xFF333333), fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                ) {
                    Icon(Icons.Default.KeyboardArrowDown, null)
                }
            }
            item {
                FabWithLabel(
                    fabBackgroundColor = Color(0xFF6DD9CC),
                    fabContentColor = Color(0xFFFF0000),
                    labelBackgroundColor = Color(0xFFFFFFFF),
                    onClick = { },
                    labelContent = {
                        Text(
                            text = "Income", color = Color(0xFF333333), fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, null)
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreenPrev()
}