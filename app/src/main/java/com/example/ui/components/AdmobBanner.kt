package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.TextMuted
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdmobBanner(
    adUnitId: String = "ca-app-pub-6914219637245532/6058636642",
    modifier: Modifier = Modifier
) {
    val isInEditMode = LocalInspectionMode.current
    if (isInEditMode) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(DarkSurface),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "AdMob Banner Ad", color = TextMuted, fontSize = 12.sp)
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkSurface),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    // Use sample test banner in debug mode if requested ID is pending approval, or live ID in production
                    this.adUnitId = if (com.example.BuildConfig.DEBUG) {
                        "ca-app-pub-3940256099942544/6300978111" // Google's official AdMob test banner ID
                    } else {
                        adUnitId
                    }
                    this.adListener = object : com.google.android.gms.ads.AdListener() {
                        override fun onAdFailedToLoad(loadAdError: com.google.android.gms.ads.LoadAdError) {
                            android.util.Log.w("AdmobBanner", "Ad failed to load: ${loadAdError.message}")
                        }
                    }
                    post {
                        try {
                            loadAd(AdRequest.Builder().build())
                        } catch (e: Exception) {
                            android.util.Log.w("AdmobBanner", "loadAd exception", e)
                        }
                    }
                }
            }
        )
    }
}
