package com.ayush.network.domain.usecase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) { supabaseClient.auth.signOut() }
    }
}