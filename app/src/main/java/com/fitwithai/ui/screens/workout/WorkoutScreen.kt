package com.fitwithai.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WorkoutScreen() {

    val primaryGreen = Color(0xFF11A36A)

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
                    selected = true,
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
                    selected = false,
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FBFA))
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Workout",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF072B4F)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = primaryGreen,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Top Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    WorkoutInfoItem(
                        title = "Workout Type",
                        value = "Walk",
                        icon = Icons.Outlined.DirectionsWalk,
                        color = primaryGreen
                    )

                    WorkoutInfoItem(
                        title = "Duration",
                        value = "20:00",
                        icon = Icons.Outlined.Timer,
                        color = primaryGreen
                    )

                    WorkoutInfoItem(
                        title = "Goal",
                        value = "5000 Steps",
                        icon = Icons.Outlined.ShowChart,
                        color = primaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Circular Progress
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(320.dp)
            ) {

                CircularProgressIndicator(
                    progress = { 0.25f },
                    modifier = Modifier.fillMaxSize(),
                    color = primaryGreen,
                    strokeWidth = 12.dp,
                    trackColor = Color(0xFFE8F5EE),
                    strokeCap = StrokeCap.Round
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Outlined.DirectionsWalk,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(42.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Steps",
                        fontSize = 24.sp,
                        color = primaryGreen,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "1,250",
                        fontSize = 62.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF072B4F)
                    )

                    Text(
                        text = "/ 5000",
                        fontSize = 30.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Timer
            Text(
                text = "Time",
                color = primaryGreen,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "00:08:35",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF072B4F)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Stats Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    WorkoutStatItem(
                        title = "Pace",
                        value = "5'40''",
                        unit = "min/km",
                        color = primaryGreen
                    )

                    WorkoutStatItem(
                        title = "Calories",
                        value = "68",
                        unit = "kcal",
                        color = primaryGreen
                    )

                    WorkoutStatItem(
                        title = "Distance",
                        value = "0.85",
                        unit = "km",
                        color = primaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .height(65.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.5.dp
                    )
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Pause,
                        contentDescription = null,
                        tint = primaryGreen
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Pause",
                        fontSize = 22.sp,
                        color = primaryGreen
                    )
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .height(65.dp),
                    shape = RoundedCornerShape(24.dp),
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
                        text = "Finish",
                        fontSize = 22.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutInfoItem(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WorkoutStatItem(
    title: String,
    value: String,
    unit: String,
    color: Color
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title,
            fontSize = 20.sp,
            color = color,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = value,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF072B4F)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = unit,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}