@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import net.chmielowski.randomchoice.R
import androidx.compose.material.MaterialTheme as MaterialTheme2

@Composable
internal fun AppTheme(content: @Composable () -> Unit) {
    val isDark = isDark(LocalTheme.current)
    MaterialTheme(
        colorScheme = colorScheme(isDark),
        typography = typography(),
    ) {
        MaterialTheme2(
            colors = colors(isDark),
            content = content,
        )
    }
}

@Composable
private fun isDark(theme: Theme) = when (theme) {
    Theme.Light -> false
    Theme.Dark -> true
    Theme.System -> isSystemInDarkTheme()
}

@Composable
private fun typography() = Typography(
    headlineMedium = MaterialTheme.typography.headlineMedium
        .copy(
            fontFamily = FontFamily(Font(R.font.jaapokki_regular)),
            fontSize = 32.sp,
        ),
    titleLarge = MaterialTheme.typography.titleLarge
        .copy(
            fontFamily = FontFamily(Font(R.font.jaapokki_regular)),
        ),
)

@Composable
private fun colors(darkTheme: Boolean) = if (darkTheme) {
    darkColors()
} else {
    lightColors()
}.copy(
    primary = MaterialTheme.colorScheme.primary,
    background = MaterialTheme.colorScheme.background,
)
