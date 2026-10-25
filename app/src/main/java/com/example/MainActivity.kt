package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ui.screens.ScriptGeneratorScreen
import com.example.ui.screens.ThumbnailGeneratorScreen
import com.example.ui.screens.VideoGeneratorScreen
import com.example.ui.screens.VoiceStudioScreen
import com.example.ui.screens.YouTubePostScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextMuted
import com.example.viewmodel.ViralCutViewModel

enum class NavScreen(val labelRes: Int) {
    CREATE(R.string.nav_create),
    SCRIPTS(R.string.nav_scripts),
    STUDIO(R.string.nav_studio),
    THUMBNAILS(R.string.nav_thumbnails),
    PUBLISH(R.string.nav_publish),
    ACCOUNT(R.string.nav_account)
}

class MainActivity : ComponentActivity() {

    private val viewModel: ViralCutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: ViralCutViewModel) {
    var currentScreen by rememberSaveable { mutableStateOf(NavScreen.CREATE) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        bottomBar = {
            androidx.compose.foundation.layout.Column {
                com.example.ui.components.AdmobBanner()
                NavigationBar(
                    containerColor = DarkSurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                NavScreen.values().forEach { screen ->
                    val isSelected = currentScreen == screen
                    val icon = when (screen) {
                        NavScreen.CREATE -> Icons.Default.AutoAwesome
                        NavScreen.SCRIPTS -> Icons.Default.Description
                        NavScreen.STUDIO -> Icons.Default.RecordVoiceOver
                        NavScreen.THUMBNAILS -> Icons.Default.Image
                        NavScreen.PUBLISH -> Icons.Default.SmartDisplay
                        NavScreen.ACCOUNT -> Icons.Default.Person
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            viewModel.stopVideoPlayback()
                            currentScreen = screen
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = stringResource(screen.labelRes)
                            )
                        },
                        label = {
                            Text(text = stringResource(screen.labelRes))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = NeonRed,
                            indicatorColor = NeonRed,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
                    )
                }
            }
        }
    }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                NavScreen.CREATE -> VideoGeneratorScreen(
                    viewModel = viewModel,
                    onNavigateToPublish = { currentScreen = NavScreen.PUBLISH }
                )
                NavScreen.SCRIPTS -> ScriptGeneratorScreen(
                    viewModel = viewModel,
                    onNavigateToStudio = { currentScreen = NavScreen.CREATE }
                )
                NavScreen.STUDIO -> VoiceStudioScreen(viewModel = viewModel)
                NavScreen.THUMBNAILS -> ThumbnailGeneratorScreen(viewModel = viewModel)
                NavScreen.PUBLISH -> YouTubePostScreen(viewModel = viewModel)
                NavScreen.ACCOUNT -> com.example.ui.screens.LoginScreen(
                    onSignInSuccess = { currentScreen = NavScreen.CREATE },
                    onContinueAsGuest = { currentScreen = NavScreen.CREATE }
                )
            }
        }
    }
}
