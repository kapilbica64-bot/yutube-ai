package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory

class ViralCutApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        
        // Initialize MobileAds asynchronously to prevent UI/Chromium seed loader blocking
        Thread {
            try {
                val requestConfiguration = com.google.android.gms.ads.RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf(com.google.android.gms.ads.AdRequest.DEVICE_ID_EMULATOR))
                    .build()
                com.google.android.gms.ads.MobileAds.setRequestConfiguration(requestConfiguration)

                com.google.android.gms.ads.MobileAds.initialize(this) { status ->
                    Log.d("ViralCutApp", "MobileAds initialized: $status")
                }
            } catch (e: Exception) {
                Log.w("ViralCutApp", "MobileAds init error", e)
            }
        }.start()

        val appCheck = FirebaseAppCheck.getInstance()
        val siteKey = try {
            BuildConfig.RECAPTCHA_ENTERPRISE_SITE_KEY
        } catch (e: Exception) {
            ""
        }

        if (BuildConfig.DEBUG) {
            // For local development and debug builds in the emulator
            appCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
            Log.d("ViralCutApp", "App Check initialized with DebugAppCheckProviderFactory")
        } else if (!siteKey.isNullOrEmpty()) {
            // For production with reCAPTCHA Enterprise / Fraud Defense site key
            appCheck.installAppCheckProviderFactory(
                RecaptchaAppCheckProviderFactory.getInstance(siteKey)
            )
            Log.d("ViralCutApp", "App Check initialized with RecaptchaAppCheckProviderFactory")
        }
    }
}
