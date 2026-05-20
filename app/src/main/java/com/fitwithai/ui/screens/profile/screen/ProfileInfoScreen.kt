package com.example.fitwithai.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddProfileInfoScreen(
    onSaveClick: () -> Unit = {}
) {

    var firstName by remember { mutableStateOf("Ravindra") }
    var lastName by remember { mutableStateOf("Singh") }
    var height by remember { mutableStateOf("165") }
    var weight by remember { mutableStateOf("66") }
    var gender by remember { mutableStateOf("Male") }
    var dob by remember { mutableStateOf("06 | 10 | 1994") }

    val primaryGreen = Color(0xFF10A36C)

    val bmi = remember(height, weight) {
        calculateBMI(
            height.toFloatOrNull() ?: 0f,
            weight.toFloatOrNull() ?: 0f
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Add Profile Info",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF072B4F)
                )

                TextButton(
                    onClick = onSaveClick
                ) {
                    Text(
                        text = "Save",
                        color = primaryGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Name Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                CustomTextField(
                    modifier = Modifier.weight(1f),
                    label = "First Name",
                    value = firstName,
                    onValueChange = { firstName = it }
                )

                CustomTextField(
                    modifier = Modifier.weight(1f),
                    label = "Last Name",
                    value = lastName,
                    onValueChange = { lastName = it }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Enter your details to calculate metrics like stride length and calories.",
                fontSize = 17.sp,
                color = Color.Gray,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Height & Weight
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                CustomTextField(
                    modifier = Modifier.weight(1f),
                    label = "Height",
                    value = "$height cm",
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        height = it.filter { c ->
                            c.isDigit()
                        }
                    }
                )

                CustomTextField(
                    modifier = Modifier.weight(1f),
                    label = "Weight",
                    value = "$weight kg",
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        weight = it.filter { c ->
                            c.isDigit()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Gender
            DropdownField(
                label = "Sex",
                value = gender
            )

            Spacer(modifier = Modifier.height(24.dp))

            // DOB
            DateField(
                label = "Date of Birth",
                value = dob
            )

            Spacer(modifier = Modifier.height(50.dp))

            // BMI Section
            Text(
                text = "Your BMI",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 22.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = bmi,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = primaryGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "66kg | 165 cm | Male | 31 yr old",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 18.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(40.dp))

            // BMI Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            ) {

                BMIBar(Color(0xFF3B82F6), 1f)
                BMIBar(Color(0xFF65D300), 1f)
                BMIBar(Color(0xFFFFD600), 1f)
                BMIBar(Color(0xFFFF9800), 1f)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "You are Normal",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = primaryGreen
            )
        }
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {

    val primaryGreen = Color(0xFF10A36C)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = {
            Text(text = label)
        },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryGreen,
            unfocusedBorderColor = primaryGreen
        )
    )
}

@Composable
fun DropdownField(
    label: String,
    value: String
) {

    val primaryGreen = Color(0xFF10A36C)

    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(text = label)
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = null,
                tint = primaryGreen
            )
        },
        readOnly = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryGreen,
            unfocusedBorderColor = primaryGreen
        )
    )
}

@Composable
fun DateField(
    label: String,
    value: String
) {

    val primaryGreen = Color(0xFF10A36C)

    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(text = label)
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = null,
                tint = primaryGreen
            )
        },
        readOnly = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryGreen,
            unfocusedBorderColor = primaryGreen
        )
    )
}

@Composable
fun BMIBar(
    color: Color,
    weight: Float
) {

    Box(
        modifier = Modifier
          //  .weight(weight)
            .fillMaxHeight()
            .background(color)
    )
}

fun calculateBMI(
    heightCm: Float,
    weightKg: Float
): String {

    if (heightCm == 0f) return "0.0"

    val heightMeter = heightCm / 100
    val bmi = weightKg / (heightMeter * heightMeter)

    return String.format("%.1f", bmi)
}