package cn.corremoon.bikubiku.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cn.corremoon.bikubiku.R
import kotlinx.coroutines.delay
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "Rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "IconRotation"
    )

    // 用于渐隐动画的 alpha 值
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        delay(800) // 展示动画 0.8 秒
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(500) // 0.5 秒渐隐
        )
        onFinished()
    }

    val isMonet = MiuixTheme.isDynamicColor

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MiuixTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        // 使用 graphicsLayer 应用渐隐和旋转，同时增大尺寸至 128.dp
        Box(
            modifier = Modifier
                .size(128.dp)
                .graphicsLayer {
                    this.alpha = alpha.value
                    this.rotationZ = rotation
                }
                .clip(CircleShape)
                .background(if (isMonet) MiuixTheme.colorScheme.primaryContainer else MiuixTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Splash Logo",
                modifier = Modifier.fillMaxSize(),
                colorFilter = if (isMonet) ColorFilter.tint(MiuixTheme.colorScheme.onPrimaryContainer) else null
            )
        }
    }
}
