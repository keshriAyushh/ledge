package com.ayush.common.auth

import kotlinx.coroutines.flow.Flow

sealed interface AuthState {
    data object Loading : AuthState
    data object Authenticated : AuthState
    data object NotAuthenticated : AuthState
}

interface AuthStateProvider {
    val authState: Flow<AuthState>
    suspend fun validateSession()
}
