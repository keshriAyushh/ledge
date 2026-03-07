package com.ayush.common.deeplink

import android.content.Intent

interface DeepLinkHandler {
    fun handle(intent: Intent)
}
