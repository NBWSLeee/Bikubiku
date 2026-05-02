package cn.corremoon.bikubiku

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cn.corremoon.bikubiku.ui.MainScreen
import cn.corremoon.bikubiku.ui.components.SplashScreen
import cn.corremoon.bikubiku.ui.theme.BikubikuTheme
import cn.corremoon.bikubiku.ui.theme.BikubikuThemeMode
import cn.corremoon.bikubiku.ui.theme.isInDarkTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    private val themeModeKey = intPreferencesKey("theme_mode")

    override fun onCreate(savedInstanceState: Bundle?) {
        // 同步读取配置以消除启动闪烁
        val savedModeIndex = runBlocking {
            dataStore.data.map { it[themeModeKey] ?: 0 }.first()
        }
        val initialThemeMode = BikubikuThemeMode.entries[savedModeIndex]

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var appState by remember { mutableStateOf(AppState(themeMode = initialThemeMode)) }
            var showSplash by remember { mutableStateOf(true) }
            val scope = rememberCoroutineScope()

            val isDark = isInDarkTheme(appState.themeMode)
            SideEffect {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { isDark },
                    navigationBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { isDark }
                )
            }

            val updateAppState: ((AppState) -> AppState) -> Unit = remember {
                { transform ->
                    val newState = transform(appState)
                    if (newState.themeMode != appState.themeMode) {
                        scope.launch {
                            dataStore.edit { it[themeModeKey] = newState.themeMode.ordinal }
                        }
                    }
                    appState = newState
                }
            }

            CompositionLocalProvider(
                LocalAppState provides appState,
                LocalUpdateAppState provides updateAppState,
            ) {
                BikubikuTheme(themeMode = appState.themeMode) {
                    if (showSplash) {
                        SplashScreen(onFinished = { showSplash = false })
                    } else {
                        MainScreen()
                    }
                }
            }
        }
    }
}
