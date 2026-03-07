package com.ayush.network.data.repository

import com.ayush.common.models.User
import com.ayush.common.result.ApiResult
import com.ayush.network.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): ApiResult<User> = withContext(Dispatchers.IO) {
        runCatching {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            getCurrentUser()
        }.fold(
            onSuccess = { user ->
                user?.let { ApiResult.Success(it) } ?: ApiResult.Error("Sign-in succeeded but no user in session")
            },
            onFailure = { e -> ApiResult.Error(e.message ?: "Sign-in failed", e) }
        )
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): ApiResult<User> = withContext(Dispatchers.IO) {
        runCatching {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            getCurrentUser()
        }.fold(
            onSuccess = { user ->
                user?.let { ApiResult.Success(it) }
                    ?: ApiResult.Error("Sign-up succeeded but email confirmation may be required")
            },
            onFailure = { e -> ApiResult.Error(e.message ?: "Sign-up failed", e) }
        )
    }

    override suspend fun getCurrentUser(): User? {
        return withContext(Dispatchers.IO) {
            supabaseClient.auth.currentUserOrNull()?.let { currentUser ->
                val metadata = currentUser.userMetadata
                User(
                    id = currentUser.id,
                    email = currentUser.email ?: "",
                    fullName = metadata?.get("full_name")?.toString()?.trim('"') ?: "User",
                    avatarUrl = metadata?.get("avatar_url")?.toString()?.trim('"')?.takeIf { it != "null" },
                    isEmailVerified = currentUser.emailConfirmedAt != null
                )
            } ?: run {
                // fetch from db. if null there as well, return null finally
                null
            }
        }
    }

    override suspend fun signInWithGoogleIdToken(idToken: String): ApiResult<User> = withContext(Dispatchers.IO) {
        runCatching {
            supabaseClient.auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
            }
            getCurrentUser()
        }.fold(
            onSuccess = { user ->
                user?.let { ApiResult.Success(it) } ?: ApiResult.Error("Sign-in succeeded but no user in session")
            },
            onFailure = { e -> ApiResult.Error(e.message ?: "Google sign-in failed", e) }
        )
    }


    override suspend fun resetPasswordForEmail(email: String): ApiResult<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            supabaseClient.auth.resetPasswordForEmail(email)
        }.fold(
            onSuccess = { ApiResult.Success(Unit) },
            onFailure = { ApiResult.Error(it.message ?: "Password reset failed", it) }
        )
    }

    override suspend fun signOut() {
        withContext(Dispatchers.IO) { supabaseClient.auth.signOut() }
    }
}