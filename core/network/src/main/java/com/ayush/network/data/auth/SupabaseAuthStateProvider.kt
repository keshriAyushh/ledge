package com.ayush.network.data.auth

import android.content.Context
import com.ayush.common.auth.AuthState
import com.ayush.common.auth.AuthStateProvider
import com.ayush.common.utils.toast
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class SupabaseAuthStateProvider @Inject constructor(
    private val supabaseClient: SupabaseClient,
    @param:ApplicationContext private val context: Context
) : AuthStateProvider {
    override val authState: Flow<AuthState> = supabaseClient.auth.sessionStatus.map { status ->
        when (status) {
            is SessionStatus.Authenticated -> AuthState.Authenticated
            is SessionStatus.NotAuthenticated -> AuthState.NotAuthenticated
            SessionStatus.Initializing -> AuthState.Loading
            else -> AuthState.NotAuthenticated
        }
    }

    override suspend fun validateSession() {
        if (supabaseClient.auth.currentSessionOrNull() == null) return
        try {
            supabaseClient.auth.retrieveUserForCurrentSession(updateSession = true)
        } catch (e: Exception) {
            Timber.w(e, "Session validation failed, signing out")
            try {
                supabaseClient.auth.signOut()
                "Logged out successfully".toast(context)
            } catch (_: Exception) {
            }
        }
    }
}
