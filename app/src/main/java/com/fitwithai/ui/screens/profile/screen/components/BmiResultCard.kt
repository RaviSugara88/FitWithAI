package com.fitwithai.ui.screens.profile.screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun BmiResultCard(
    weightKg: Double,
    heightCm: Double,
    sex: String,
    age: Int
) {
    // Calculate BMI
    val heightMeters = heightCm / 100
    val bmi = if (heightMeters > 0) weightKg / (heightMeters * heightMeters) else 0.0
    val roundedBmi = (bmi * 10.0).roundToInt() / 10.0 // Round to 1 decimal place

    // Determine Category and Colors
    val (category, categoryColor) = when {
        bmi < 18.5 -> "Underweight" to Color(0xFF4A90E2) // Blue
        bmi < 25.0 -> "Normal" to Color(0xFF7ED321)      // Green
        bmi < 30.0 -> "Overweight" to Color(0xFFF8E71C)  // Yellow
        bmi < 35.0 -> "Obese" to Color(0xFFF5A623)       // Orange
        else -> "Extremely Obese" to Color(0xFFD0021B)   // Red
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your BMI",
                fontSize = 20.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Light
            )

            Text(
                text = roundedBmi.toString(),
                fontSize = 64.sp,
                color = Color(0xFF4A148C), // Deep Purple
                fontWeight = FontWeight.Bold
            )

            // Subtitle Details (e.g. "90kg | 5ft | Female | 34yr old")
            Text(
                text = "${weightKg.toInt()}kg | ${(heightCm / 30.48).toInt()}ft | $sex | ${age}yr old",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // The Segmented Progress Bar
            BmiProgressBar(bmi = bmi)

            Spacer(modifier = Modifier.height(24.dp))

            // Highlighted Result Text
            Text(
                text = buildAnnotatedString {
                    append("You are ")
                    withStyle(style = SpanStyle(color = categoryColor, fontWeight = FontWeight.Bold)) {
                        append(category)
                    }
                },
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun BmiProgressBar(bmi: Double) {
    val segments = listOf(
        Color(0xFF4A90E2), // Underweight (< 18.5)
        Color(0xFF7ED321), // Normal (18.5 - 25)
        Color(0xFFF8E71C), // Overweight (25 - 30)
        Color(0xFFF5A623), // Obese (30 - 35)
        Color(0xFFD0021B)  // Extremely Obese (> 35)
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        val segmentWidth = size.width / segments.size
        val barHeight = 8.dp.toPx()
        val yOffset = (size.height - barHeight) / 2

        // Draw the segments
        segments.forEachIndexed { index, color ->
            drawRoundRect(
                color = color,
                topLeft = Offset(x = index * segmentWidth, y = yOffset),
                size = Size(width = segmentWidth - 2.dp.toPx(), height = barHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
        }

        // Calculate Marker Position
        // Assuming visual range on this specific bar is approx BMI 15 to 40
        val minBmi = 15.0
        val maxBmi = 40.0
        val clampedBmi = bmi.coerceIn(minBmi, maxBmi)
        val progressPercent = ((clampedBmi - minBmi) / (maxBmi - minBmi)).toFloat()
        val markerX = (size.width * progressPercent).coerceIn(0f, size.width)

        // Draw Marker (Orange rectangle with white border)
        val markerWidth = 4.dp.toPx()
        val markerHeight = 20.dp.toPx()
        
        // White Outline
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(x = markerX - (markerWidth/2) - 2f, y = 0f - 2f),
            size = Size(width = markerWidth + 4f, height = markerHeight + 4f),
            cornerRadius = CornerRadius(2.dp.toPx())
        )
        // Orange Inner Marker
        drawRoundRect(
            color = Color(0xFFF5A623),
            topLeft = Offset(x = markerX - (markerWidth/2), y = 0f),
            size = Size(width = markerWidth, height = markerHeight),
            cornerRadius = CornerRadius(2.dp.toPx())
        )
    }
}