package com.example.launcher_for_tv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LauncherForTVTheme(
    isInDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isInDarkTheme) {
        darkColorScheme(
            primary = Color(0xFFBB86FC),
            secondary = Color(0xFF3700B3),
            tertiary = Color(0xFF03DAC5),
            background = Color.Black,
            surface = Color.DarkGray
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC5),
            tertiary = Color(0xFF018786),
            background = Color.Black,
            surface = Color.LightGray
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
