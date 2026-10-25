package com.example.model

data class VideoScene(
    val id: String,
    val sceneNumber: Int,
    val narration: String,
    val visualPrompt: String,
    val durationSeconds: Int = 5,
    val transition: String = "Zoom In", // Zoom In, Fade, Slide Left, Pan Right, Glitch
    val backgroundTheme: String = "Cyber Neon", // Cyber Neon, Dark Sunset, Golden Hour, Electric Blue, Minimal Noir
    val captionHighlightWord: String = ""
)

data class GeneratedVideoProject(
    val id: String,
    val title: String,
    val topic: String,
    val category: String,
    val targetPlatform: String = "YouTube Shorts (9:16)",
    val totalDurationSeconds: Int = 30,
    val hook: String,
    val callToAction: String,
    val description: String,
    val hashtags: List<String>,
    val tags: List<String>,
    val scenes: List<VideoScene>,
    val voiceLanguage: String = "English (US)",
    val voiceGender: String = "Male Dynamic",
    val voiceSpeed: Float = 1.1f,
    val voicePitch: Float = 1.0f,
    val backgroundMusic: String = "Energetic Lo-Fi Beats",
    val captionStyle: CaptionStyle = CaptionStyle.MR_BEAST_BOLD,
    val generatedThumbnailUrl: String? = null,
    val thumbnailPrompts: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

enum class CaptionStyle(val displayName: String, val textColor: Long, val strokeColor: Long, val highlightColor: Long) {
    MR_BEAST_BOLD("Bold Yellow Impact", 0xFFFFF500, 0xFF000000, 0xFFFF0055),
    NEON_CYAN("Cyberpunk Cyan", 0xFF00F0FF, 0xFF000000, 0xFFFF00A0),
    CLEAN_WHITE("Modern Minimal", 0xFFFFFFFF, 0x88000000, 0xFF6C5CE7),
    VIBRANT_FIRE("Trending Crimson Flame", 0xFFFF3838, 0xFF000000, 0xFFFFFA65)
}

data class ScriptContentPackage(
    val topic: String,
    val category: String,
    val viralHooks: List<String>,
    val fullScript: String,
    val scenePrompts: List<String>,
    val youtubeTitles: List<String>,
    val description: String,
    val hashtags: List<String>,
    val keywords: List<String>,
    val callToActions: List<String>
)

data class VoiceProfile(
    val id: String,
    val name: String,
    val language: String,
    val gender: String,
    val localeTag: String,
    val sampleText: String
)

data class ThumbnailConcept(
    val id: String,
    val titleOverlay: String,
    val visualPrompt: String,
    val colorPaletteName: String,
    val bgGradientStart: Long,
    val bgGradientEnd: Long,
    val badgeText: String = "SHORTS"
)

enum class VideoVisibility {
    PUBLIC,
    UNLISTED,
    PRIVATE
}

data class YouTubeUploadRequest(
    val videoTitle: String,
    val description: String,
    val tags: List<String>,
    val visibility: VideoVisibility,
    val isScheduled: Boolean,
    val scheduledEpochMillis: Long? = null,
    val isMadeForKids: Boolean = false,
    val categoryId: String = "22" // People & Blogs or Entertainment
)

data class YouTubeUploadStatus(
    val isUploading: Boolean = false,
    val progressPercent: Int = 0,
    val statusMessage: String = "",
    val videoId: String? = null,
    val videoUrl: String? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
