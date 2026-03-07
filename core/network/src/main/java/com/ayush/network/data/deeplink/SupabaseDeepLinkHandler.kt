package com.ayush.network.data.deeplink

import android.content.Intent
import com.ayush.common.deeplink.DeepLinkHandler
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import javax.inject.Inject

class SupabaseDeepLinkHandler @Inject constructor(
    private val supabaseClient: SupabaseClient
) : DeepLinkHandler {
    override fun handle(intent: Intent) {
        supabaseClient.handleDeeplinks(intent)
    }
}
