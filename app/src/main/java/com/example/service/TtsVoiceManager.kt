package com.example.service

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.model.VoiceProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TtsVoiceManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentUtteranceId = MutableStateFlow("")
    val currentUtteranceId: StateFlow<String> = _currentUtteranceId.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.language = Locale.US
                setupListener()
            }
        }
    }

    private fun setupListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = true
                _currentUtteranceId.value = utteranceId ?: ""
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
            }

            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
            }
        })
    }

    fun getVoiceProfiles(): List<VoiceProfile> {
        return listOf(
            VoiceProfile("en_us_male", "Alex (Dynamic Hype)", "English", "Male", "en_US", "Stop scrolling! Here is what nobody tells you about going viral today."),
            VoiceProfile("en_us_female", "Sophia (Narrator Smooth)", "English", "Female", "en_US", "Have you ever wondered what happens behind the scenes of viral creators?"),
            VoiceProfile("en_uk_male", "Arthur (Documentary Deep)", "English (UK)", "Male", "en_GB", "In 1945, a strange phenomenon occurred that scientists could not explain."),
            VoiceProfile("hi_in_male", "Aarav (Energetic Hindi)", "Hindi (हिन्दी)", "Male", "hi_IN", "रुको! क्या आपको पता है कि यूट्यूब शॉर्ट्स में यह ट्रिक कैसे काम करती है?"),
            VoiceProfile("hi_in_female", "Priya (Expressive Hindi)", "Hindi (हिन्दी)", "Female", "hi_IN", "नमस्ते दोस्तों! आज हम जानेंगे दुनिया के सबसे अनोखे और रहस्यमयी तथ्यों के बारे में।"),
            VoiceProfile("ne_np_male", "Bikash (Authentic Nepali)", "Nepali (नेपाली)", "Male", "ne_NP", "पख्नुहोस्! के तपाईंलाई थाहा छ यो गोप्य तरिकाले कसरी भिडियो भाइरल बनाउन सकिन्छ?"),
            VoiceProfile("ne_np_female", "Samikshya (Gentle Nepali)", "Nepali (नेपाली)", "Female", "ne_NP", "नमस्ते सबैजनालाई! आज म तपाईंहरूलाई एकदमै रोचक तथ्य बताउन गइरहेकी छु।")
        )
    }

    fun speak(
        text: String,
        voiceProfile: VoiceProfile,
        speed: Float = 1.1f,
        pitch: Float = 1.0f,
        utteranceId: String = "narration_${System.currentTimeMillis()}"
    ) {
        if (!isInitialized || tts == null) return

        try {
            val localeParts = voiceProfile.localeTag.split("_")
            val locale = if (localeParts.size == 2) {
                Locale(localeParts[0], localeParts[1])
            } else {
                Locale(voiceProfile.localeTag)
            }
            tts?.language = locale
            tts?.setSpeechRate(speed.coerceIn(0.5f, 2.0f))
            tts?.setPitch(pitch.coerceIn(0.5f, 2.0f))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            } else {
                @Suppress("DEPRECATION")
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, hashMapOf(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID to utteranceId))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
