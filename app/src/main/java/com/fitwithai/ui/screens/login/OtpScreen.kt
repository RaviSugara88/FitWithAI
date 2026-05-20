package com.fitwithai.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpScreen(
    phoneNumber: String = "+91 98765 43210",
    onBackClick: () -> Unit = {},
    onOtpComplete: (String) -> Unit = {}
) {

    val otpValues = remember {
        mutableStateListOf("", "", "", "", "", "")
    }

    val primaryGreen = Color(0xFF10A36C)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            // Back Button
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null,
                    tint = primaryGreen
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logo
            Text(
                text = "FitWithAI",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = primaryGreen,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Title
            Text(
                text = "OTP Verification",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003B2A),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter the 6-digit code sent to",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = phoneNumber,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = primaryGreen,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(50.dp))

            // OTP Fields
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                otpValues.forEachIndexed { index, value ->

                    OtpInputBox(
                        value = value,
                        onValueChange = {

                            if (it.length <= 1) {
                                otpValues[index] = it

                                if (otpValues.all { otp ->
                                        otp.isNotEmpty()
                                    }
                                ) {
                                    onOtpComplete(
                                        otpValues.joinToString("")
                                    )
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Resend Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Didn't receive the code? ",
                    color = Color.Gray,
                    fontSize = 17.sp
                )

                Text(
                    text = "Resend OTP",
                    color = primaryGreen,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Resend in 00:28",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Security Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF7FFFB))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Outlined.Security,
                    contentDescription = null,
                    tint = primaryGreen,
                    modifier = Modifier.size(70.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Secure Verification",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003B2A)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Enter the OTP to verify your number\nand secure your account.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 17.sp,
                    lineHeight = 28.sp
                )
            }
        }
    }
}

@Composable
fun OtpInputBox(
    value: String,
    onValueChange: (String) -> Unit
) {

    val primaryGreen = Color(0xFF10A36C)

    Box(
        modifier = Modifier
            .size(56.dp, 70.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.5.dp,
                color = primaryGreen,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= 1) {
                    onValueChange(it)
                }
            },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}