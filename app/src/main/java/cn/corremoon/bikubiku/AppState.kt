package cn.corremoon.bikubiku

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import cn.corremoon.bikubiku.ui.theme.BikubikuThemeMode

@Stable
data class AppState(
    val themeMode: BikubikuThemeMode = BikubikuThemeMode.System
)

val LocalAppState = compositionLocalOf<AppState> {
    error("No AppState provided!")
}

val LocalUpdateAppState = staticCompositionLocalOf<((AppState) -> AppState) -> Unit> {
    error("No AppState updater provided!")
}
