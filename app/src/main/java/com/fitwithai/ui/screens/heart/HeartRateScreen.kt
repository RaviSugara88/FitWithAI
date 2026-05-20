package com.fitwithai.ui.screens.heart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeartRateScreen() {

    val primaryGreen = Color(0xFF10A36C)

    var selectedTab by remember {
        mutableStateOf(0)
    }

    Scaffold(
        containerColor = Color(0xFFF9FBFA)
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
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

                    IconButton(
                        onClick = {}
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            tint = primaryGreen
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Heart Rate Tracking",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0A1F44)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {}
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null,
                            tint = primaryGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                ) {

                    TabButton(
                        modifier = Modifier.weight(1f),
                        title = "Measure",
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                        }
                    )

                    TabButton(
                        modifier = Modifier.weight(1f),
                        title = "Statistics",
                        selected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Main Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Place your finger on the camera\nlens and stay still",
                            fontSize = 24.sp,
                            lineHeight = 34.sp,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(40.dp))

                        // Heart Animation Circle
                        Box(
                            modifier = Modifier.size(320.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            repeat(3) { index ->

                                Box(
                                    modifier = Modifier
                                        .size((320 - (index * 50)).dp)
                                        .clip(CircleShape)
                                        .background(
                                            Color.Transparent
                                        )
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .clip(CircleShape)
                                    .background(
                                        primaryGreen.copy(alpha = 0.08f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Outlined.Favorite,
                                    contentDescription = null,
                                    tint = primaryGreen,
                                    modifier = Modifier.size(90.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = "Measuring...",
                            color = primaryGreen,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {

                            Text(
                                text = "72",
                                fontSize = 82.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0A1F44)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {

                                Icon(
                                    imageVector = Icons.Outlined.Favorite,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(34.dp)
                                )

                                Text(
                                    text = "BPM",
                                    color = primaryGreen,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Keep still for accurate result",
                            color = Color.Gray,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        // Stats Row
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF8FFFB)
                            )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                HeartStatItem(
                                    title = "Min",
                                    value = "58"
                                )

                                HeartStatItem(
                                    title = "Max",
                                    value = "98"
                                )

                                HeartStatItem(
                                    title = "Avg",
                                    value = "72"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        // Stop Button
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryGreen
                            )
                        ) {

                            Icon(
                                imageVector = Icons.Outlined.Stop,
                                contentDescription = null,
                                tint = Color.White
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "Stop Measuring",
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        // Tips Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF9FFFC)
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        imageVector = Icons.Outlined.GraphicEq,
                                        contentDescription = null,
                                        tint = primaryGreen
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = "Tips for accurate reading",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                TipItem("Keep your finger steady and still")
                                TipItem("Ensure good lighting")
                                TipItem("Make sure the camera lens is clean")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun TabButton(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    val primaryGreen = Color(0xFF10A36C)

    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) primaryGreen else Color.White,
            contentColor = if (selected) Color.White else Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (selected) 2.dp else 0.dp
        )
    ) {

        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HeartStatItem(
    title: String,
    value: String
) {

    val primaryGreen = Color(0xFF10A36C)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title,
            color = primaryGreen,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = value,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0A1F44)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "BPM",
            color = Color.Gray,
            fontSize = 18.sp
        )
    }
}

@Composable
fun TipItem(
    text: String
) {

    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF10A36C))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.DarkGray
        )
    }
}