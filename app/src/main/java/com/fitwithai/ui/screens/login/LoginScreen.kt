package com.fitwithai.ui.screens.login

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitwithai.R
import com.fitwithai.data.auth.GoogleCredentialProvider
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private val ScreenBackground = Color(0xFFF9F5EF)
private val TitleGreen = Color(0xFF1B3022)
private val ButtonBorder = Color(0xFFD9D0C4)
private val MutedText = Color(0xFF6B6560)
private val OrLine = Color(0xFFC9C2BA)

private val PillShape = RoundedCornerShape(percent = 50)
private val ButtonHeight = 54.dp

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToDashboard: () -> Unit,
) {
    val googleCredentialProvider = koinInject<GoogleCredentialProvider>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalContext.current as ComponentActivity
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.NavigateToDashboard -> onNavigateToDashboard()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ScreenBackground,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.login_screen),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentScale = ContentScale.Fit,
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Sign up or log in",
                    modifier = Modifier.fillMaxWidth(),
                    color = TitleGreen,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.Center,
                )
                uiState.errorMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = msg,
                        color = Color(0xFFB3261E),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))

                Column(
                    modifier = Modifier.widthIn(max = 400.dp).padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SocialPillButton(
                        backgroundColor = Color.White,
                        borderColor = ButtonBorder,
                        onClick = {
                            if (uiState.isLoading) return@SocialPillButton
                            scope.launch {
                                try {
                                    val token = googleCredentialProvider.getGoogleIdToken(activity)
                                    viewModel.onGoogleIdToken(token)
                                } catch (e: Exception) {
                                    viewModel.reportError(e.message ?: "Google sign-in failed")
                                }
                            }
                        },
                        enabled = !uiState.isLoading,
                        iconPainter = painterResource(R.drawable.ic_google),
                        iconContentDescription = null,
                        label = "Continue with Google",
                        labelColor = MutedText,
                    )
                    SocialPillButton(
                        backgroundColor = Color.White,
                        borderColor = ButtonBorder,
                        onClick = {
                            if (uiState.isLoading) return@SocialPillButton
                            viewModel.onInstagramSignIn(activity)
                        },
                        enabled = !uiState.isLoading,
                        iconPainter = painterResource(R.drawable.ic_instagram),
                        iconContentDescription = null,
                        label = "Continue with Instagram",
                        labelColor = MutedText,
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
                OrDivider(modifier = Modifier.fillMaxWidth(0.92f))
                Spacer(modifier = Modifier.height(28.dp))

                SocialPillButton(
                    modifier = Modifier.widthIn(max = 400.dp),
                    backgroundColor = Color.White,
                    borderColor = ButtonBorder,
                    onClick = {
                        if (uiState.isLoading) return@SocialPillButton
                        viewModel.onUsePhoneNumber()
                    },
                    enabled = !uiState.isLoading,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Smartphone,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MutedText,
                        )
                    },
                    label = "Use phone number",
                    labelColor = MutedText,
                )
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = TitleGreen)
                }
            }
        }
    }
}

@Composable
private fun OrDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = OrLine,
            thickness = 1.dp,
        )
        Text(
            text = "or",
            modifier = Modifier.padding(horizontal = 14.dp),
            color = MutedText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = OrLine,
            thickness = 1.dp,
        )
    }
}

@Composable
private fun SocialPillButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: String,
    labelColor: Color,
    iconPainter: Painter? = null,
    iconContentDescription: String? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    val shape = PillShape
    val alpha = if (enabled) 1f else 0.5f
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight)
            .clip(shape)
            .background(backgroundColor.copy(alpha = alpha))
            .border(width = 1.dp, color = borderColor.copy(alpha = alpha), shape = shape)
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        if (iconPainter != null) {
            Image(
                painter = iconPainter,
                contentDescription = iconContentDescription,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
                    .size(24.dp),
            )
        } else if (icon != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp),
            ) {
                icon()
            }
        }
        Text(
            text = label,
            modifier = Modifier.align(Alignment.Center),
            color = labelColor.copy(alpha = alpha),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
    }
}
