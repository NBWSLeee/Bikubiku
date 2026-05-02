package cn.corremoon.bikubiku.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

enum class BikubikuThemeMode {
    System, Light, Dark, MonetSystem, MonetLight, MonetDark
}

@Composable
fun BikubikuTheme(
    themeMode: BikubikuThemeMode = BikubikuThemeMode.System,
    keyColor: Color? = null,
    content: @Composable () -> Unit
) {
    val controller = remember(themeMode, keyColor) {
        val mode = when (themeMode) {
            BikubikuThemeMode.System -> ColorSchemeMode.System
            BikubikuThemeMode.Light -> ColorSchemeMode.Light
            BikubikuThemeMode.Dark -> ColorSchemeMode.Dark
            BikubikuThemeMode.MonetSystem -> ColorSchemeMode.MonetSystem
            BikubikuThemeMode.MonetLight -> ColorSchemeMode.MonetLight
            BikubikuThemeMode.MonetDark -> ColorSchemeMode.MonetDark
        }
        ThemeController(mode, keyColor = keyColor)
    }

    MiuixTheme(
        controller = controller,
        content = content
    )
}

@Composable
@ReadOnlyComposable
fun isInDarkTheme(themeMode: BikubikuThemeMode): Boolean = when (themeMode) {
    BikubikuThemeMode.Light, BikubikuThemeMode.MonetLight -> false
    BikubikuThemeMode.Dark, BikubikuThemeMode.MonetDark -> true
    else -> isSystemInDarkTheme()
}