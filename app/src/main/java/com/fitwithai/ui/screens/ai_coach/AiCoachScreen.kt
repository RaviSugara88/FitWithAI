package com.fitwithai.ui.screens.ai_coach


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF16B364)
private val LightGreen = Color(0xFFE8FFF2)
private val TextGray = Color(0xFF667085)
private val BorderColor = Color(0xFFE4E7EC)

@Composable
fun AiCoachScreen() {

    val chartData = listOf(
        65f, 75f, 70f, 82f, 68f, 76f, 74f, 81f,
        73f, 79f, 69f, 85f, 102f, 78f, 82f, 71f,
        81f, 77f, 72f, 76f, 82f, 69f, 75f, 66f
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar()
        },
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // TOP BAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "AI Coach",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = null,
                        tint = Green,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // HEART CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Your Result",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )

                        Text(
                            text = "May 4, 2026",
                            color = TextGray,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(LightGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Green
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Heart Rate",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Normal range: 60-100 BPM",
                                color = Green,
                                fontSize = 14.sp
                            )
                        }

                        Text(
                            text = "72",
                            color = Green,
                            fontWeight = FontWeight.Bold,
                            fontSize = 42.sp
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "BPM",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    HeartRateChart(
                        data = chartData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text(
                            text = "✨ AI Insights",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DETAILS BUTTON
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White
                )
            ) {

                Text(
                    text = "More details about my heart rate",
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = TextGray
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ANALYSIS CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = "Your most recent heart data from 2026.05.04 provides a comprehensive snapshot of your cardiovascular status, including heart rate, HRV, stress, and energy.",
                        fontSize = 14.sp,
                        color = Color.Black,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    SectionTitle(title = "📊 Analyze")

                    BulletText(
                        "Heart Rate (72 bpm): This value is within the typical resting range for adults."
                    )

                    BulletText(
                        "HRV (28 ms): Heart rate variability is on the lower end."
                    )

                    BulletText(
                        "Stress Level (86): This index remains high and may indicate strain."
                    )

                    BulletText(
                        "Energy Level (29): Current energy index is on the lower side."
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    SectionTitle(title = "💡 Recommendations")

                    BulletText(
                        "Establish regular sleep patterns to improve recovery."
                    )

                    BulletText(
                        "Practice relaxation techniques to manage stress."
                    )

                    BulletText(
                        "Engage in moderate physical activity regularly."
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    SectionTitle(title = "⚠ Important Notice")

                    Text(
                        text = "This information is for reference only and should not replace professional medical diagnosis or treatment.",
                        fontSize = 14.sp,
                        color = TextGray,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    SectionTitle(title = "❓ You may also want to know:")

                    Text(
                        text = "Do you need me to explain the impact of stress and energy levels on your heart health?",
                        fontSize = 14.sp,
                        color = Color.Black,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ACTION BUTTONS
            val actions = listOf(
                "Health Check",
                "FAQ",
                "Rate",
                "Export"
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(actions) { item ->

                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(20.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White
                        )
                    ) {

                        Text(
                            text = item,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // CHAT INPUT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = BorderColor,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Type your question here",
                    color = TextGray,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Green),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {

    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun BulletText(text: String) {

    Row(
        modifier = Modifier.padding(bottom = 12.dp)
    ) {

        Text(
            text = "• ",
            color = Green,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Text(
            text = text,
            color = Color.Black,
            lineHeight = 22.sp,
            fontSize = 14.sp
        )
    }
}

@Composable
fun HeartRateChart(
    data: List<Float>,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val maxValue = 120f
        val minValue = 40f

        val width = size.width
        val height = size.height

        val spacing = width / (data.size - 1)

        val path = Path()
        val fillPath = Path()

        data.forEachIndexed { index, value ->

            val x = index * spacing

            val y =
                height - ((value - minValue) / (maxValue - minValue) * height)

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }

            if (index == data.lastIndex) {
                fillPath.lineTo(x, height)
                fillPath.close()
            }
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Green.copy(alpha = 0.35f),
                    Green.copy(alpha = 0.05f)
                )
            )
        )

        drawPath(
            path = path,
            color = Green,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )

        // Highlight point
        val highlightIndex = 12

        val pointX = highlightIndex * spacing

        val pointY =
            height - ((data[highlightIndex] - minValue) /
                    (maxValue - minValue) * height)

        drawCircle(
            color = Green,
            radius = 12f,
            center = Offset(pointX, pointY)
        )

        drawCircle(
            color = Color.White,
            radius = 5f,
            center = Offset(pointX, pointY)
        )
    }
}

@Composable
fun BottomNavigationBar() {

    NavigationBar(
        containerColor = Color.White
    ) {

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(Icons.Outlined.Info, null)
            },
            label = {
                Text("Home")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(Icons.Default.Favorite, null)
            },
            label = {
                Text("Workout")
            }
        )

        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = {
                Icon(Icons.Default.Favorite, null)
            },
            label = {
                Text("AI Coach")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(Icons.Default.Favorite, null)
            },
            label = {
                Text("Progress")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(Icons.Default.Favorite, null)
            },
            label = {
                Text("Profile")
            }
        )
    }
}