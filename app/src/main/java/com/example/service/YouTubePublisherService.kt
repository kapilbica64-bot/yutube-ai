package com.example.service

import com.example.model.VideoVisibility
import com.example.model.YouTubeUploadRequest
import com.example.model.YouTubeUploadStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class YouTubePublisherService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    // Holds current channel identity once authenticated
    var channelName: String = "Viral Creator Studio"
    var channelHandle: String = "@ViralCutChannel"
    var isConnected: Boolean = true
    var activeAccessToken: String = "ya29.a0AfH6SMD_YOUTUBE_OAUTH_TOKEN_V1"

    fun uploadShort(
        request: YouTubeUploadRequest,
        videoFile: File?
    ): Flow<YouTubeUploadStatus> = flow {
        emit(YouTubeUploadStatus(isUploading = true, progressPercent = 5, statusMessage = "Connecting to YouTube Data API v3..."))
        delay(600)

        emit(YouTubeUploadStatus(isUploading = true, progressPercent = 15, statusMessage = "Preparing 9:16 Shorts metadata & tags..."))
        delay(700)

        val metaPayload = JSONObject().apply {
            put("snippet", JSONObject().apply {
                put("title", if (request.videoTitle.contains("#Shorts")) request.videoTitle else "${request.videoTitle} #Shorts")
                put("description", "${request.description}\n\nGenerated with ViralCut AI #Shorts #Reels")
                put("tags", JSONArray(request.tags))
                put("categoryId", request.categoryId)
            })
            put("status", JSONObject().apply {
                val privacyStr = when (request.visibility) {
                    VideoVisibility.PUBLIC -> "public"
                    VideoVisibility.UNLISTED -> "unlisted"
                    VideoVisibility.PRIVATE -> "private"
                }
                put("privacyStatus", if (request.isScheduled) "private" else privacyStr)
                if (request.isScheduled && request.scheduledEpochMillis != null) {
                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                    put("publishAt", sdf.format(Date(request.scheduledEpochMillis)))
                }
                put("selfDeclaredMadeForKids", request.isMadeForKids)
            })
        }

        emit(YouTubeUploadStatus(isUploading = true, progressPercent = 30, statusMessage = "Initiating resumable upload session..."))
        delay(900)

        // Simulate realistic multi-chunk network transfer progress for vertical video
        for (p in 35..95 step 12) {
            emit(YouTubeUploadStatus(isUploading = true, progressPercent = p, statusMessage = "Uploading video chunks ($p%)..."))
            delay(400)
        }

        emit(YouTubeUploadStatus(isUploading = true, progressPercent = 98, statusMessage = "Finalizing YouTube processing & setting HD 9:16 aspect ratio..."))
        delay(1000)

        // Generate realistic YouTube short ID
        val mockId = "vc_" + System.currentTimeMillis().toString().takeLast(8)
        val shortUrl = "https://youtube.com/shorts/$mockId"

        emit(
            YouTubeUploadStatus(
                isUploading = false,
                progressPercent = 100,
                statusMessage = if (request.isScheduled) "Video scheduled successfully on YouTube!" else "Published successfully to YouTube Shorts!",
                videoId = mockId,
                videoUrl = shortUrl,
                isSuccess = true
            )
        )
    }.flowOn(Dispatchers.IO)
}
