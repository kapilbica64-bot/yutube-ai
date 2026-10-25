package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.CaptionStyle
import com.example.model.GeneratedVideoProject
import com.example.model.ScriptContentPackage
import com.example.model.ThumbnailConcept
import com.example.model.VideoScene
import com.example.model.VideoVisibility
import com.example.model.VoiceProfile
import com.example.model.YouTubeUploadRequest
import com.example.model.YouTubeUploadStatus
import com.example.service.GeminiService
import com.example.service.TtsVoiceManager
import com.example.service.YouTubePublisherService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID

class ViralCutViewModel(application: Application) : AndroidViewModel(application) {

    private val geminiService = GeminiService()
    val ttsManager = TtsVoiceManager(application)
    val youtubeService = YouTubePublisherService()

    // Currently active created video project
    private val _currentProject = MutableStateFlow<GeneratedVideoProject?>(null)
    val currentProject: StateFlow<GeneratedVideoProject?> = _currentProject.asStateFlow()

    // Loading states
    private val _isGeneratingVideo = MutableStateFlow(false)
    val isGeneratingVideo: StateFlow<Boolean> = _isGeneratingVideo.asStateFlow()

    private val _generationStep = MutableStateFlow("")
    val generationStep: StateFlow<String> = _generationStep.asStateFlow()

    // Script generator state
    private val _isGeneratingScript = MutableStateFlow(false)
    val isGeneratingScript: StateFlow<Boolean> = _isGeneratingScript.asStateFlow()

    private val _scriptPackage = MutableStateFlow<ScriptContentPackage?>(null)
    val scriptPackage: StateFlow<ScriptContentPackage?> = _scriptPackage.asStateFlow()

    // Thumbnail concepts
    private val _thumbnailConcepts = MutableStateFlow<List<ThumbnailConcept>>(emptyList())
    val thumbnailConcepts: StateFlow<List<ThumbnailConcept>> = _thumbnailConcepts.asStateFlow()

    private val _selectedThumbnail = MutableStateFlow<ThumbnailConcept?>(null)
    val selectedThumbnail: StateFlow<ThumbnailConcept?> = _selectedThumbnail.asStateFlow()

    // Voice settings
    val voiceProfiles: List<VoiceProfile> = ttsManager.getVoiceProfiles()
    private val _selectedVoice = MutableStateFlow<VoiceProfile>(voiceProfiles.first())
    val selectedVoice: StateFlow<VoiceProfile> = _selectedVoice.asStateFlow()

    private val _voiceSpeed = MutableStateFlow(1.1f)
    val voiceSpeed: StateFlow<Float> = _voiceSpeed.asStateFlow()

    private val _voicePitch = MutableStateFlow(1.0f)
    val voicePitch: StateFlow<Float> = _voicePitch.asStateFlow()

    // Caption Styling
    private val _selectedCaptionStyle = MutableStateFlow(CaptionStyle.MR_BEAST_BOLD)
    val selectedCaptionStyle: StateFlow<CaptionStyle> = _selectedCaptionStyle.asStateFlow()

    // Video Player & Scene Playback Simulation
    private val _activeSceneIndex = MutableStateFlow(0)
    val activeSceneIndex: StateFlow<Int> = _activeSceneIndex.asStateFlow()

    private val _isPlayingVideo = MutableStateFlow(false)
    val isPlayingVideo: StateFlow<Boolean> = _isPlayingVideo.asStateFlow()

    private var playbackJob: Job? = null

    // YouTube Upload Status
    private val _uploadStatus = MutableStateFlow(YouTubeUploadStatus())
    val uploadStatus: StateFlow<YouTubeUploadStatus> = _uploadStatus.asStateFlow()

    init {
        // Initialize with default thumbnail concepts
        generateThumbnailConcepts("AI Money Secrets 2026")
        // Initialize default sample project for instant preview
        createSampleProject()
    }

    private fun createSampleProject() {
        val sampleScenes = listOf(
            VideoScene(
                id = "s1",
                sceneNumber = 1,
                narration = "Wait! Stop scrolling right now before it's too late.",
                visualPrompt = "Dynamic neon glitch effect with high-contrast text overlay",
                durationSeconds = 5,
                transition = "Zoom In",
                backgroundTheme = "Cyber Neon",
                captionHighlightWord = "WAIT"
            ),
            VideoScene(
                id = "s2",
                sceneNumber = 2,
                narration = "99% of creators don't know this secret YouTube shorts formula.",
                visualPrompt = "Futuristic analytics dashboard spiking to 1 million views",
                durationSeconds = 6,
                transition = "Slide Left",
                backgroundTheme = "Electric Blue",
                captionHighlightWord = "SECRET"
            ),
            VideoScene(
                id = "s3",
                sceneNumber = 3,
                narration = "Drop an explosive hook in 2 seconds, cut dead silence, and sync beats.",
                visualPrompt = "Fast paced kinetic editing timeline with audio spectrum waves",
                durationSeconds = 7,
                transition = "Glitch",
                backgroundTheme = "Dark Sunset",
                captionHighlightWord = "EXPLOSIVE"
            ),
            VideoScene(
                id = "s4",
                sceneNumber = 4,
                narration = "Double tap and subscribe right now to unlock the full blueprint!",
                visualPrompt = "Glowing golden subscribe button with particle explosion",
                durationSeconds = 5,
                transition = "Zoom In",
                backgroundTheme = "Minimal Noir",
                captionHighlightWord = "SUBSCRIBE"
            )
        )

        _currentProject.value = GeneratedVideoProject(
            id = UUID.randomUUID().toString(),
            title = "The Viral Formula 99% Of Creators Ignore! 🚀",
            topic = "YouTube Shorts Algorithm Growth Hacks",
            category = "Technology",
            targetPlatform = "YouTube Shorts (9:16)",
            totalDurationSeconds = 23,
            hook = "Wait! Stop scrolling right now before it's too late.",
            callToAction = "Double tap and subscribe right now to unlock the full blueprint!",
            description = "Discover the exact viral retention formula used by top short-form creators. #Shorts #ViralHacks #ViralCutAI",
            hashtags = listOf("#Shorts", "#Viral", "#CreatorEconomy", "#AI", "#GrowthHacks"),
            tags = listOf("youtube shorts", "viral hooks", "shorts algorithm", "video editing", "viralcut ai"),
            scenes = sampleScenes,
            voiceLanguage = "English (US)",
            voiceGender = "Male Dynamic",
            backgroundMusic = "Energetic Lo-Fi Beats"
        )
    }

    fun setVoice(profile: VoiceProfile) {
        _selectedVoice.value = profile
    }

    fun setVoiceSpeed(speed: Float) {
        _voiceSpeed.value = speed
    }

    fun setVoicePitch(pitch: Float) {
        _voicePitch.value = pitch
    }

    fun setCaptionStyle(style: CaptionStyle) {
        _selectedCaptionStyle.value = style
    }

    fun setSelectedThumbnail(thumb: ThumbnailConcept) {
        _selectedThumbnail.value = thumb
    }

    fun playVoiceSample(profile: VoiceProfile) {
        ttsManager.speak(profile.sampleText, profile, _voiceSpeed.value, _voicePitch.value)
    }

    fun stopVoice() {
        ttsManager.stop()
    }

    fun generateFullVideoProject(
        topic: String,
        category: String,
        duration: Int = 30,
        targetPlatform: String = "YouTube Shorts (9:16)"
    ) {
        viewModelScope.launch {
            _isGeneratingVideo.value = true
            _generationStep.value = "Analyzing viral trends & audience retention pattern..."
            delay(600)

            _generationStep.value = "Crafting hypnotic hook & scene-by-scene script..."
            val prompt = """
                You are a world-class viral short video producer.
                Generate a viral short video package for the topic: "$topic" in category "$category" targeted for $duration seconds ($targetPlatform).
                Return strictly a JSON object with this exact structure:
                {
                  "hook": "Strong 2-second hook line",
                  "title": "Viral Click-worthy Title with #Shorts",
                  "description": "Engaging description with keywords",
                  "hashtags": ["#Shorts", "#Tag1", "#Tag2"],
                  "tags": ["keyword1", "keyword2", "keyword3"],
                  "callToAction": "Clear subscribe / like CTA",
                  "scenes": [
                    {
                      "sceneNumber": 1,
                      "narration": "First spoken line",
                      "visualPrompt": "Detailed visual description of 9:16 scene",
                      "durationSeconds": 5,
                      "transition": "Zoom In",
                      "backgroundTheme": "Cyber Neon",
                      "captionHighlightWord": "FIRST"
                    }
                  ]
                }
            """.trimIndent()

            val aiResponse = geminiService.generateContent(prompt)
            _generationStep.value = "Generating auto-captions and syncing audio narration..."
            delay(500)

            _generationStep.value = "Composing 9:16 vertical render and audio beat matching..."
            delay(600)

            try {
                // Clean markdown code blocks if present
                val cleanedJson = aiResponse.replace("```json", "").replace("```", "").trim()
                val jsonObject = JSONObject(cleanedJson)

                val hook = jsonObject.optString("hook", "Stop scrolling right now!")
                val title = jsonObject.optString("title", "$topic #Shorts")
                val desc = jsonObject.optString("description", "Watch till the end to discover the truth. #Shorts")
                val cta = jsonObject.optString("callToAction", "Subscribe for more viral insights!")

                val hashtagsList = mutableListOf<String>()
                val hashArr = jsonObject.optJSONArray("hashtags")
                if (hashArr != null) {
                    for (i in 0 until hashArr.length()) {
                        hashtagsList.add(hashArr.getString(i))
                    }
                }
                if (hashtagsList.isEmpty()) hashtagsList.addAll(listOf("#Shorts", "#Viral", "#Trending"))

                val tagsList = mutableListOf<String>()
                val tagsArr = jsonObject.optJSONArray("tags")
                if (tagsArr != null) {
                    for (i in 0 until tagsArr.length()) {
                        tagsList.add(tagsArr.getString(i))
                    }
                }
                if (tagsList.isEmpty()) tagsList.addAll(listOf("shorts", "viral tricks", "trending"))

                val scenesList = mutableListOf<VideoScene>()
                val scenesArr = jsonObject.optJSONArray("scenes")
                if (scenesArr != null) {
                    val themes = listOf("Cyber Neon", "Dark Sunset", "Electric Blue", "Minimal Noir", "Golden Hour")
                    val transitions = listOf("Zoom In", "Slide Left", "Glitch", "Pan Right", "Fade")

                    for (i in 0 until scenesArr.length()) {
                        val sObj = scenesArr.getJSONObject(i)
                        scenesList.add(
                            VideoScene(
                                id = UUID.randomUUID().toString(),
                                sceneNumber = sObj.optInt("sceneNumber", i + 1),
                                narration = sObj.optString("narration", "Scene content for topic"),
                                visualPrompt = sObj.optString("visualPrompt", "Cinematic 9:16 vertical shot"),
                                durationSeconds = sObj.optInt("durationSeconds", 5),
                                transition = sObj.optString("transition", transitions[i % transitions.size]),
                                backgroundTheme = sObj.optString("backgroundTheme", themes[i % themes.size]),
                                captionHighlightWord = sObj.optString("captionHighlightWord", "")
                            )
                        )
                    }
                }

                if (scenesList.isEmpty()) {
                    // Fallback to structured scenes
                    scenesList.add(VideoScene(UUID.randomUUID().toString(), 1, hook, "Cinematic hook opener", 5, "Zoom In", "Cyber Neon", "ATTENTION"))
                    scenesList.add(VideoScene(UUID.randomUUID().toString(), 2, "Here is the key insight you cannot afford to miss.", "Key value delivery", 6, "Slide Left", "Electric Blue", "INSIGHT"))
                    scenesList.add(VideoScene(UUID.randomUUID().toString(), 3, cta, "Outro with subscribe prompt", 5, "Zoom In", "Dark Sunset", "SUBSCRIBE"))
                }

                val totalDuration = scenesList.sumOf { it.durationSeconds }

                val project = GeneratedVideoProject(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    topic = topic,
                    category = category,
                    targetPlatform = targetPlatform,
                    totalDurationSeconds = totalDuration,
                    hook = hook,
                    callToAction = cta,
                    description = desc,
                    hashtags = hashtagsList,
                    tags = tagsList,
                    scenes = scenesList,
                    voiceLanguage = _selectedVoice.value.language,
                    voiceGender = _selectedVoice.value.gender,
                    voiceSpeed = _voiceSpeed.value,
                    voicePitch = _voicePitch.value,
                    captionStyle = _selectedCaptionStyle.value
                )

                _currentProject.value = project
                generateThumbnailConcepts(topic)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isGeneratingVideo.value = false
                _generationStep.value = ""
            }
        }
    }

    fun generateScriptContentPackage(topic: String, category: String) {
        viewModelScope.launch {
            _isGeneratingScript.value = true
            val prompt = """
                Generate a comprehensive viral script & content strategy for a 9:16 short video on the topic: "$topic" in category "$category".
                Include:
                - 3 viral hooks that stop scrolling
                - Full short script (spoken word, under 60 seconds)
                - 4 scene-by-scene prompts for visual rendering
                - 3 high CTR YouTube titles
                - SEO optimized YouTube Shorts description
                - 6 high-ranking hashtags
                - 6 keyword tags
                - 3 call-to-action variants
                
                Respond in valid JSON format:
                {
                  "viralHooks": ["Hook 1", "Hook 2", "Hook 3"],
                  "fullScript": "Complete spoken script text...",
                  "scenePrompts": ["Scene 1 prompt", "Scene 2 prompt", "Scene 3 prompt", "Scene 4 prompt"],
                  "youtubeTitles": ["Title 1", "Title 2", "Title 3"],
                  "description": "Shorts description...",
                  "hashtags": ["#tag1", "#tag2", "#tag3", "#tag4", "#tag5", "#tag6"],
                  "keywords": ["kw1", "kw2", "kw3", "kw4", "kw5", "kw6"],
                  "callToActions": ["CTA 1", "CTA 2", "CTA 3"]
                }
            """.trimIndent()

            val aiResponse = geminiService.generateContent(prompt)
            try {
                val cleanedJson = aiResponse.replace("```json", "").replace("```", "").trim()
                val obj = JSONObject(cleanedJson)

                fun jsonArrayToList(arrName: String): List<String> {
                    val arr = obj.optJSONArray(arrName) ?: return emptyList()
                    val list = mutableListOf<String>()
                    for (i in 0 until arr.length()) list.add(arr.getString(i))
                    return list
                }

                _scriptPackage.value = ScriptContentPackage(
                    topic = topic,
                    category = category,
                    viralHooks = jsonArrayToList("viralHooks"),
                    fullScript = obj.optString("fullScript", "Viral script ready for production!"),
                    scenePrompts = jsonArrayToList("scenePrompts"),
                    youtubeTitles = jsonArrayToList("youtubeTitles"),
                    description = obj.optString("description", "Viral Shorts content"),
                    hashtags = jsonArrayToList("hashtags"),
                    keywords = jsonArrayToList("keywords"),
                    callToActions = jsonArrayToList("callToActions")
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isGeneratingScript.value = false
            }
        }
    }

    fun convertScriptToAnimatedVideo(pkg: ScriptContentPackage) {
        val themes = listOf("Cyber Neon", "Dark Sunset", "Electric Blue", "Minimal Noir", "Golden Hour")
        val transitions = listOf("Zoom In", "Slide Left", "Glitch", "Pan Right", "Fade")

        val generatedScenes = mutableListOf<VideoScene>()
        val hook = pkg.viralHooks.firstOrNull() ?: "Stop scrolling right now!"
        val cta = pkg.callToActions.firstOrNull() ?: "Subscribe and share for more!"

        // Scene 1: Hook
        generatedScenes.add(
            VideoScene(
                id = UUID.randomUUID().toString(),
                sceneNumber = 1,
                narration = hook,
                visualPrompt = pkg.scenePrompts.getOrNull(0) ?: "Dynamic glowing typography opener with particle blast",
                durationSeconds = 5,
                transition = "Zoom In",
                backgroundTheme = "Cyber Neon",
                captionHighlightWord = "WAIT"
            )
        )

        // Middle scenes split from fullScript or scenePrompts
        val sentences = pkg.fullScript.split(". ").filter { it.isNotBlank() }
        val contentSentences = if (sentences.size > 2) sentences.drop(1).dropLast(1) else sentences
        contentSentences.take(3).forEachIndexed { i, sentence ->
            val scenePrompt = pkg.scenePrompts.getOrNull(i + 1) ?: "3D animated kinetic scene with high-speed cut"
            generatedScenes.add(
                VideoScene(
                    id = UUID.randomUUID().toString(),
                    sceneNumber = generatedScenes.size + 1,
                    narration = sentence.trim().removeSuffix("."),
                    visualPrompt = scenePrompt,
                    durationSeconds = 6,
                    transition = transitions[(i + 1) % transitions.size],
                    backgroundTheme = themes[(i + 1) % themes.size],
                    captionHighlightWord = sentence.split(" ").firstOrNull { it.length > 4 }?.uppercase() ?: "INSIGHT"
                )
            )
        }

        // Outro Scene: CTA
        generatedScenes.add(
            VideoScene(
                id = UUID.randomUUID().toString(),
                sceneNumber = generatedScenes.size + 1,
                narration = cta,
                visualPrompt = pkg.scenePrompts.lastOrNull() ?: "Subscribe button with golden particle animation",
                durationSeconds = 5,
                transition = "Zoom In",
                backgroundTheme = "Minimal Noir",
                captionHighlightWord = "SUBSCRIBE"
            )
        )

        val totalSecs = generatedScenes.sumOf { it.durationSeconds }
        val title = pkg.youtubeTitles.firstOrNull() ?: "${pkg.topic} #Shorts"

        val project = GeneratedVideoProject(
            id = UUID.randomUUID().toString(),
            title = title,
            topic = pkg.topic,
            category = pkg.category,
            targetPlatform = "YouTube Shorts (9:16)",
            totalDurationSeconds = totalSecs,
            hook = hook,
            callToAction = cta,
            description = "${pkg.description}\n\nGenerated with ViralCut AI #Shorts #Reels",
            hashtags = pkg.hashtags,
            tags = pkg.keywords,
            scenes = generatedScenes,
            voiceLanguage = _selectedVoice.value.language,
            voiceGender = _selectedVoice.value.gender,
            voiceSpeed = _voiceSpeed.value,
            voicePitch = _voicePitch.value,
            captionStyle = _selectedCaptionStyle.value
        )

        _currentProject.value = project
        generateThumbnailConcepts(pkg.topic)
    }

    fun generateThumbnailConcepts(topic: String) {
        val concepts = listOf(
            ThumbnailConcept(
                id = "t1",
                titleOverlay = topic.take(24).uppercase(),
                visualPrompt = "Bold high contrast neon text with glowing outline, shocked facial reaction, vibrant cinematic background",
                colorPaletteName = "Electric Flame",
                bgGradientStart = 0xFFFF0055,
                bgGradientEnd = 0xFFFF9900,
                badgeText = "VIRAL"
            ),
            ThumbnailConcept(
                id = "t2",
                titleOverlay = "SECRET EXPOSED!",
                visualPrompt = "Mysterious glowing vault opening with golden sparkles and neon question mark",
                colorPaletteName = "Cyber Matrix",
                bgGradientStart = 0xFF00F0FF,
                bgGradientEnd = 0xFF7000FF,
                badgeText = "SHORTS"
            ),
            ThumbnailConcept(
                id = "t3",
                titleOverlay = "DO NOT MISS THIS!",
                visualPrompt = "Hyper-realistic red warning badge with 3D trending arrow pointing up",
                colorPaletteName = "Ultra Danger",
                bgGradientStart = 0xFFFF1E56,
                bgGradientEnd = 0xFF0F1016,
                badgeText = "1M+ VIEWS"
            ),
            ThumbnailConcept(
                id = "t4",
                titleOverlay = "100X RESULTS ⚡",
                visualPrompt = "Lightning bolts striking exponential graph with emerald neon illumination",
                colorPaletteName = "Neon Emerald",
                bgGradientStart = 0xFF00FF87,
                bgGradientEnd = 0xFF60EFFF,
                badgeText = "TOP 1%"
            )
        )
        _thumbnailConcepts.value = concepts
        _selectedThumbnail.value = concepts.first()
    }

    fun startVideoPlayback() {
        val project = _currentProject.value ?: return
        if (project.scenes.isEmpty()) return

        stopVideoPlayback()
        _isPlayingVideo.value = true

        playbackJob = viewModelScope.launch {
            while (_isPlayingVideo.value) {
                for (index in project.scenes.indices) {
                    if (!_isPlayingVideo.value) break
                    _activeSceneIndex.value = index
                    val scene = project.scenes[index]
                    // Play narration via TTS
                    ttsManager.speak(scene.narration, _selectedVoice.value, _voiceSpeed.value, _voicePitch.value)
                    delay(scene.durationSeconds * 1000L)
                }
                // Loop video or finish
                _isPlayingVideo.value = false
            }
        }
    }

    fun stopVideoPlayback() {
        _isPlayingVideo.value = false
        playbackJob?.cancel()
        playbackJob = null
        ttsManager.stop()
    }

    fun uploadToYouTube(request: YouTubeUploadRequest) {
        viewModelScope.launch {
            youtubeService.uploadShort(request, null).collect { status ->
                _uploadStatus.value = status
            }
        }
    }

    fun resetUploadStatus() {
        _uploadStatus.value = YouTubeUploadStatus()
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
        stopVideoPlayback()
    }
}
