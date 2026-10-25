package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CaptionStyle
import com.example.model.GeneratedVideoProject
import com.example.model.VideoScene

@Composable
fun VideoPlayer916(
    project: GeneratedVideoProject,
    activeSceneIndex: Int,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentScene = if (project.scenes.isNotEmpty() && activeSceneIndex in project.scenes.indices) {
        project.scenes[activeSceneIndex]
    } else {
        project.scenes.firstOrNull() ?: VideoScene("s0", 1, "Viral Short", "Cinematic Scene", 5)
    }

    // Animation for pulsing effects
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val bgBrush = when (currentScene.backgroundTheme) {
        "Cyber Neon" -> Brush.verticalGradient(listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E)))
        "Dark Sunset" -> Brush.verticalGradient(listOf(Color(0xFF4A0E2E), Color(0xFF8B0032), Color(0xFF1F0D3D)))
        "Electric Blue" -> Brush.verticalGradient(listOf(Color(0xFF001F3F), Color(0xFF003B6F), Color(0xFF0074D9)))
        "Minimal Noir" -> Brush.verticalGradient(listOf(Color(0xFF141414), Color(0xFF232526), Color(0xFF414345)))
        else -> Brush.verticalGradient(listOf(Color(0xFF1E130C), Color(0xFF9A8478), Color(0xFF232526)))
    }

    Box(
        modifier = modifier
            .shadow(16.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, Color(0xFF2C324B), RoundedCornerShape(20.dp))
            .background(bgBrush)
            .testTag("video_player_9_16")
    ) {
        // Subtle background visual particle lines
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar overlay with format badge & audio track
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.65f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF2E63))
                ) {
                    Text(
                        text = "9:16 SHORTS HD",
                        color = Color(0xFFFF2E63),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Background music",
                        tint = Color(0xFF08D9D6),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = project.backgroundMusic.take(16),
                        color = Color.White,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }

            // Center Visual Prompt Indicator & Scene Description
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Visual Cut",
                            tint = Color(0xFFFFE600),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Scene ${currentScene.sceneNumber}/${project.scenes.size}: ${currentScene.transition}",
                            color = Color(0xFFFFE600),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Dynamic Animated Viral Captions in Center/Lower 3rd
                AnimatedCaptionBox(
                    narration = currentScene.narration,
                    highlightWord = currentScene.captionHighlightWord,
                    captionStyle = project.captionStyle,
                    pulseScale = if (isPlaying) pulseScale else 1.0f
                )
            }

            // Bottom Player Controls and Timeline Progress
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
                    .padding(bottom = 8.dp)
            ) {
                // Scene Timeline Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    project.scenes.forEachIndexed { idx, sc ->
                        val active = idx == activeSceneIndex
                        Box(
                            modifier = Modifier
                                .weight(sc.durationSeconds.toFloat())
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (active) Color(0xFFFF2E63)
                                    else if (idx < activeSceneIndex) Color(0xFF08D9D6)
                                    else Color.White.copy(alpha = 0.25f)
                                )
                        )
                    }
                }

                // Play / Pause and Voice indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFF2E63),
                            modifier = Modifier
                                .size(38.dp)
                                .clickable { onTogglePlay() }
                                .testTag("play_pause_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isPlaying) "Playing Scene ${currentScene.sceneNumber}" else "Tap to Preview",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Voice",
                            tint = Color(0xFF08D9D6),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = project.voiceGender,
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedCaptionBox(
    narration: String,
    highlightWord: String,
    captionStyle: CaptionStyle,
    pulseScale: Float,
    modifier: Modifier = Modifier
) {
    val words = narration.split(" ")
    val highlightClean = highlightWord.trim().uppercase()

    Surface(
        color = Color.Black.copy(alpha = 0.55f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(captionStyle.highlightColor).copy(alpha = 0.6f)),
        modifier = modifier
            .fillMaxWidth()
            .scale(pulseScale)
            .padding(8.dp)
            .testTag("animated_caption_box")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = narration,
                    color = Color(captionStyle.textColor),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            if (highlightClean.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color(captionStyle.highlightColor),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = " $highlightClean ",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
