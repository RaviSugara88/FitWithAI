package com.fitwithai.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressScreen() {

    val primaryGreen = Color(0xFF10A36C)

    Scaffold(
        bottomBar = {

            NavigationBar(
                containerColor = Color.White
            ) {

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Home")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.FitnessCenter,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Workout")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Psychology,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "AI Coach")
                    }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.ShowChart,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Progress")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Profile")
                    }
                )
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FBFA))
                .padding(padding)
                .padding(16.dp)
        ) {

            item {

                Spacer(modifier = Modifier.height(10.dp))

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Progress",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Weekly Activity
                Text(
                    text = "Weekly Activity",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "May 28 - Jun 3, 2026",
                    color = Color.Gray,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ProgressMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Avg Heart Rate",
                        value = "68",
                        unit = "BPM"
                    )

                    ProgressMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Avg HRV",
                        value = "32",
                        unit = "ms"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ProgressMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Avg Stress",
                        value = "58",
                        unit = ""
                    )

                    ProgressMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Avg Energy",
                        value = "64",
                        unit = ""
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Heart Rate Chart
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Heart Rate (BPM)",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        // Simple Chart
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {

                            val chartValues = listOf(
                                72,
                                69,
                                71,
                                66,
                                65,
                                67,
                                64
                            )

                            chartValues.forEach {

                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height((it * 2).dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(primaryGreen.copy(alpha = 0.8f))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            listOf(
                                "Thu",
                                "Fri",
                                "Sat",
                                "Sun",
                                "Mon",
                                "Tue",
                                "Wed"
                            ).forEach {

                                Text(
                                    text = it,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Analytics Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = "HRV",
                        value = "32 ms"
                    )

                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = "Stress",
                        value = "58"
                    )

                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = "Energy",
                        value = "64"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Activity Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Activity Summary",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            CircularProgressIndicator(
                                progress = { 0.85f },
                                modifier = Modifier.size(100.dp),
                                color = primaryGreen,
                                strokeWidth = 10.dp
                            )

                            Spacer(modifier = Modifier.width(24.dp))

                            Column {

                                Text(
                                    text = "6/7 Days Active",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryGreen
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Great job! You were active on 6 out of 7 days this week.",
                                    lineHeight = 24.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Insights
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF4FFF9)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Outlined.Favorite,
                                contentDescription = null,
                                tint = primaryGreen
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "Insights",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Your heart rate and stress levels are improving.\nKeep maintaining your healthy habits!",
                            lineHeight = 28.sp,
                            color = Color.DarkGray,
                            fontSize = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProgressMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String
) {

    val primaryGreen = Color(0xFF10A36C)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {

                Text(
                    text = value,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = unit,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "↑ Improved this week",
                color = primaryGreen,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AnalyticsCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {

    val primaryGreen = Color(0xFF10A36C)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {

                listOf(30, 45, 40, 55, 42, 44, 60).forEach {

                    Box(
                        modifier = Modifier
                            .width(10.dp)
                            .height(it.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(primaryGreen.copy(alpha = 0.8f))
                    )
                }
            }
        }
    }
}