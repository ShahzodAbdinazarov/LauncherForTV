package com.example.launcher_for_tv.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.launcher_for_tv.ui.theme.LauncherForTVTheme
import com.example.launcher_for_tv.utils.DefaultLauncherHelper
import com.example.launcher_for_tv.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val defaultLauncherHelper by lazy { DefaultLauncherHelper(this) }
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        validateDefaultLauncher()

        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            val isHideBackground by viewModel.isHideBackground.collectAsState()

            LauncherForTVTheme(isInDarkTheme = isDarkTheme) {
                MyLauncherApp(
                    onSettingOpened = {
                        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
//                        intent.data = "package:${packageName}".toUri()
                        startActivity(intent)
                        viewModel.setHideBackground(true)
                    },
                    isHideBackground = isHideBackground
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
            // Home and back buttons are blocked
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setHideBackground(false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLauncherApp(
    onSettingOpened: () -> Unit,
    isHideBackground: Boolean = false
) {
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    Column {
        if (isHideBackground) {
            Box(modifier = Modifier.background(Color.Black))
        } else {
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
                            onBack = { currentScreen = Screen.Home },
                            onSettingOpened = onSettingOpened
                        )
                    }
                }
            }
        }
    }
}

enum class Screen {
    Home, Settings
}
