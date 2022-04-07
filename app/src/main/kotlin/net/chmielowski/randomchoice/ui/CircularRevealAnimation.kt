@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.hypot

@Composable
internal fun CircularRevealAnimation(color: Color, onEnd: () -> Unit) {
    val progress = remember { Animatable(0F) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawRevealingCircle(color, progress.value) },
    )
    LaunchedEffect(Unit) {
        progress.animateTo(1F, tween())
        onEnd()
    }
}

private fun DrawScope.drawRevealingCircle(color: Color, progress: Float) {
    drawCircle(
        color = color,
        radius = diagonal * progress,
        center = Offset(x = size.width, y = size.height),
    )
}

private val DrawScope.diagonal get() = hypot(size.width, size.height)
