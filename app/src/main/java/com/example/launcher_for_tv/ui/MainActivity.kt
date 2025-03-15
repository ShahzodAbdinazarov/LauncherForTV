package com.example.launcher_for_tv.ui

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.launcher_for_tv.ui.theme.LauncherForTVTheme
import com.example.launcher_for_tv.utils.DefaultLauncherHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val defaultLauncherHelper by lazy { DefaultLauncherHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        validateDefaultLauncher()

        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }

            LauncherForTVTheme(isInDarkTheme = isDarkTheme) {
                MyLauncherApp(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }

    private fun validateDefaultLauncher() {
        if (!defaultLauncherHelper.isDefaultLauncher() && defaultLauncherHelper.canRequestDefaultLauncher()) {
            val intent = defaultLauncherHelper.requestDefaultLauncherIntent()
            @Suppress("DEPRECATION")
            if (intent != null) startActivityForResult(intent, 0)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK) {
            // Home tugmachasini bloklash
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLauncherApp(isDarkTheme: Boolean, onThemeChange: () -> Unit) {
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Launcher") },
                actions = {
                    IconButton(onClick = { currentScreen = Screen.Settings }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            when (currentScreen) {
                Screen.Home -> HomeScreen()
                Screen.Settings -> SettingsScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    onBack = { currentScreen = Screen.Home }
                )
            }
        }
    }
}

enum class Screen {
    Home, Settings
}
