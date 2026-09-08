package com.fitwithai.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.fitwithai.navigation.Routes
import com.fitwithai.ui.screens.dashboard.DashboardScreen
import com.fitwithai.ui.screens.login.LoginScreen
import com.fitwithai.ui.screens.login.LoginWithPhoneScreen
import com.fitwithai.ui.screens.login.OtpScreen
import com.fitwithai.ui.screens.login.PhoneAuthEvent
import com.fitwithai.ui.screens.login.PhoneAuthViewModel
import com.fitwithai.ui.screens.splash.SplashScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    val toDashboard: () -> Unit = {
        navController.navigate(Routes.DASHBOARD) {
            popUpTo(Routes.LOGIN) { inclusive = true }
            launchSingleTop = true
        }
    }

    val toLogin: () -> Unit = {
        navController.navigate(Routes.LOGIN) {
            popUpTo(Routes.SPLASH) { inclusive = true }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onAuthenticated = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onUnauthenticated = toLogin,
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToDashboard = toDashboard,
                onNavigateToPhoneLogin = { navController.navigate(Routes.PHONE_AUTH) },
            )
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen()
        }

        navigation(startDestination = Routes.LOGIN_PHONE, route = Routes.PHONE_AUTH) {
            composable(Routes.LOGIN_PHONE) { entry ->
                PhoneLoginRoute(
                    viewModel = navController.sharedPhoneAuthViewModel(entry),
                    onOtpRequested = { navController.navigate(Routes.OTP) },
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Routes.OTP) { entry ->
                OtpRoute(
                    viewModel = navController.sharedPhoneAuthViewModel(entry),
                    onVerified = toDashboard,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}

/** One [PhoneAuthViewModel] scoped to the phone-auth graph, shared by both of its screens. */
@Composable
private fun NavHostController.sharedPhoneAuthViewModel(entry: NavBackStackEntry): PhoneAuthViewModel {
    val parentEntry = remember(entry) { getBackStackEntry(Routes.PHONE_AUTH) }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}

@Composable
private fun PhoneLoginRoute(
    viewModel: PhoneAuthViewModel,
    onOtpRequested: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is PhoneAuthEvent.OtpRequested) onOtpRequested()
        }
    }
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    LoginWithPhoneScreen(
        onSendOtpClick = { viewModel.requestOtp(it) },
        onAnotherMethodClick = onBack,
    )
}

@Composable
private fun OtpRoute(
    viewModel: PhoneAuthViewModel,
    onVerified: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is PhoneAuthEvent.LoginSuccess) onVerified()
        }
    }
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    OtpScreen(
        phoneNumber = state.phoneDisplay,
        onBackClick = onBack,
        onOtpComplete = { viewModel.verifyOtp(it) },
    )
}
