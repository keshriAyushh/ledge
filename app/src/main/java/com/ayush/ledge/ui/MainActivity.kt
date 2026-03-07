package com.ayush.ledge.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.ayush.common.auth.AuthStateProvider
import com.ayush.common.deeplink.DeepLinkHandler
import com.ayush.ui.theme.LedgeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var deepLinkHandler: DeepLinkHandler
    @Inject
    lateinit var authStateProvider: AuthStateProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deepLinkHandler.handle(intent)
        enableEdgeToEdge()
        setContent {
            LedgeTheme {
                LedgeNavGraph()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { authStateProvider.validateSession() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        deepLinkHandler.handle(intent)
    }
}