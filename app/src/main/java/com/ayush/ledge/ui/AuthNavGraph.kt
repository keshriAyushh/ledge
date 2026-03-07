package com.ayush.ledge.ui

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ayush.auth.presentation.AuthViewModel
import com.ayush.auth.presentation.SignInScreen
import com.ayush.auth.presentation.SignUpScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun AuthNavGraph(
    startDestination: LedgeRoute,
    onAuthSuccess: () -> Unit
) {

    val viewModel = androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel<AuthViewModel>()

    val backStack = rememberNavBackStack(AuthRoute.SignUp)

    fun popAuth() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<AuthRoute.SignIn> {
                SignInScreen(
                    viewModel = viewModel,
                    onAuthSuccess = onAuthSuccess,
                    onNavigateToSignUp = { backStack.add(AuthRoute.SignUp) },
                    onForgotPassword = { backStack.add(AuthRoute.ForgotPassword) },
                )
            }

            entry<AuthRoute.SignUp> {
                SignUpScreen(
                    viewModel = viewModel,
                    onAuthSuccess = onAuthSuccess,
                    onNavigateToSignIn = { backStack.add(AuthRoute.SignIn) },
                )
            }

            entry<AuthRoute.ForgotPassword> {
                ForgotPasswordScreenStub(onBack = ::popAuth)
            }
        },
        onBack = ::popAuth
    )
}

@Composable
private fun ForgotPasswordScreenStub(onBack: () -> Unit) {
    Column {
        Text("Forgot Password — stub")
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
