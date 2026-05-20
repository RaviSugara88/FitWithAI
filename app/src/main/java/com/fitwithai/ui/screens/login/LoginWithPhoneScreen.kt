package com.fitwithai.ui.screens.login

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitwithai.R

@Composable
fun LoginWithPhoneScreen(
    onSendOtpClick: (String) -> Unit = {},
    onAnotherMethodClick: () -> Unit = {}
) {

    var mobileNumber by remember {
        mutableStateOf("")
    }

    val primaryGreen = Color(0xFF0FA66A)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFF4FFF9)
                        )
                    )
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(70.dp))

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.login_screen),
                    contentDescription = "Logo",
                    modifier = Modifier.size(220.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Title
                Text(
                    text = "Login with Phone",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004D36)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle
                Text(
                    text = "Enter your mobile number and we'll\nsend you a verification code.",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(50.dp))

                // Label
                Text(
                    text = "Phone Number",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Phone Input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(
                            width = 1.5.dp,
                            color = primaryGreen,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .background(Color.White)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "India Flag",
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "+91",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .width(1.dp)
                            .background(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    OutlinedTextField(
                        value = mobileNumber,
                        onValueChange = {
                            if (it.length <= 10) {
                                mobileNumber = it.filter { char ->
                                    char.isDigit()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Enter mobile number",
                                color = Color.Gray
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // OTP Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Security,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = "We will send you a 6-digit OTP\nto verify your number.",
                        fontSize = 17.sp,
                        color = Color.Gray,
                        lineHeight = 28.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Send OTP Button
                Button(
                    onClick = {
                        onSendOtpClick(mobileNumber)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryGreen
                    )
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "Send OTP",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            imageVector = Icons.Outlined.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Divider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color.LightGray
                    )

                    Text(
                        text = "or",
                        modifier = Modifier.padding(horizontal = 14.dp),
                        color = Color.Gray,
                        fontSize = 16.sp
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color.LightGray
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Another Method Button
                Button(
                    onClick = onAnotherMethodClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = primaryGreen
                    )
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            tint = primaryGreen,
                            modifier = Modifier.size(28.dp)
                        )

                        Text(
                            text = "Continue with another method",
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp),
                            fontSize = 18.sp,
                            color = Color.Black
                        )

                        Icon(
                            imageVector = Icons.Outlined.ArrowForward,
                            contentDescription = null,
                            tint = primaryGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Security Text
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Your data is secure and encrypted",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}