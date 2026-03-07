package com.ayush.auth.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.ayush.common.result.ApiResult
import com.ayush.network.domain.usecase.AuthEligibilityResult
import com.ayush.network.domain.usecase.AuthEligibilityUseCase
import com.ayush.network.domain.usecase.AuthFlow
import com.ayush.network.domain.usecase.SignInWithEmailUseCase
import com.ayush.network.domain.usecase.SignInWithGoogleIdToken
import com.ayush.network.domain.usecase.SignUpWithEmailUseCase
import com.ayush.ui.base.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val authEligibilityUseCase: AuthEligibilityUseCase,
    private val signInWithGoogleIdTokenUseCase: SignInWithGoogleIdToken
) : BaseMviViewModel<AuthUiEvent, AuthUiState, AuthUiSideEffect>(
    initialState = AuthUiState()
) {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    override fun onEvent(event: AuthUiEvent) {
        when (event) {

            is AuthUiEvent.OnStart -> {
                viewModelScope.launch {
                    setState { copy(authFlow = event.flow) }
                }
            }

            is AuthUiEvent.EmailChanged -> {
                viewModelScope.launch {
                    setState { copy(email = event.email) }
                    evaluateEligibility()
                }
            }

            is AuthUiEvent.PasswordChanged -> {
                viewModelScope.launch {
                    setState { copy(password = event.password) }
                    evaluateEligibility()
                }
            }

            is AuthUiEvent.OnGoogleIdTokenReceived -> {
                viewModelScope.launch {
                    signInWithGoogle(event.idToken)
                }
            }

            AuthUiEvent.SignUpWithEmailClicked -> {
                viewModelScope.launch {
                    if (currentState().canProceed != AuthEligibilityResult.Eligible) return@launch

                    setState { copy(isLoading = true) }
                    val state = currentState()

                    val result = signUpWithEmailUseCase(
                        email = state.email,
                        password = state.password,
                    )
                    handleEmailAuthResult(result)
                }
            }

            AuthUiEvent.SignInWithEmailClicked -> {
                viewModelScope.launch {
                    if (currentState().canProceed != AuthEligibilityResult.Eligible) return@launch

                    setState { copy(isLoading = true) }
                    val state = currentState()

                    val result = signInWithEmailUseCase(
                        email = state.email,
                        password = state.password,
                    )
                    handleEmailAuthResult(result)
                }
            }

            AuthUiEvent.SignInWithGoogleClicked -> {
                viewModelScope.launch {
                    sendSideEffect(AuthUiSideEffect.GetGoogleSignInToken)
                }
            }

            AuthUiEvent.ResetState -> resetState()
        }
    }

    private suspend fun evaluateEligibility() {
        val state = currentState()
        val result = authEligibilityUseCase.canProceed(
            email = state.email,
            password = state.password,
            authFlow = state.authFlow,
        )
        setState { copy(canProceed = result) }
    }

    private suspend fun handleEmailAuthResult(result: ApiResult<*>) {
        setState { copy(isLoading = false) }
        when (result) {
            is ApiResult.Success -> {
                sendSideEffect(
                    AuthUiSideEffect.NotifySignInStateUpdate(SignInState.Success)
                )
            }

            is ApiResult.Error -> {
                sendSideEffect(
                    AuthUiSideEffect.NotifySignInStateUpdate(
                        SignInState.Failed(result.cause)
                    )
                )
            }
        }
    }

    private suspend fun signInWithGoogle(idToken: String?) {
        if (idToken == null) {
            sendSideEffect(AuthUiSideEffect.ShowToast("Google sign-in failed"))
            Timber.tag(TAG).e("Google sign-in failed — id token was null")
            return
        }

        setState { copy(isLoading = true) }

        when (val result = signInWithGoogleIdTokenUseCase(idToken)) {
            is ApiResult.Success -> {
                setState { copy(isLoading = false) }
                sendSideEffect(
                    AuthUiSideEffect.NotifySignInStateUpdate(SignInState.Success)
                )
            }

            is ApiResult.Error -> {
                setState { copy(isLoading = false) }
                sendSideEffect(
                    AuthUiSideEffect.NotifySignInStateUpdate(
                        SignInState.Failed(result.cause)
                    )
                )
            }
        }
    }
}

sealed interface AuthUiEvent {
    data class OnStart(val flow: AuthFlow) : AuthUiEvent
    data class EmailChanged(val email: String) : AuthUiEvent
    data class PasswordChanged(val password: String) : AuthUiEvent
    data class OnGoogleIdTokenReceived(val idToken: String) : AuthUiEvent

    data object SignUpWithEmailClicked : AuthUiEvent
    data object SignInWithEmailClicked : AuthUiEvent
    data object SignInWithGoogleClicked : AuthUiEvent
    data object ResetState : AuthUiEvent
}

sealed interface AuthUiSideEffect {
    data class ShowToast(val message: String) : AuthUiSideEffect

    data class NotifySignInStateUpdate(
        val signInState: SignInState
    ) : AuthUiSideEffect

    data object GetGoogleSignInToken : AuthUiSideEffect
}

sealed interface SignInState {
    data object Success : SignInState
    data class Failed(val exception: Throwable?) : SignInState
}

@Stable
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val canProceed: AuthEligibilityResult = AuthEligibilityResult.Nothing,
    val authFlow: AuthFlow = AuthFlow.SIGN_IN,
)


