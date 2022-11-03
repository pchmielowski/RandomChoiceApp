@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import net.chmielowski.randomchoice.R

@Composable
internal fun AppTheme(content: @Composable () -> Unit) {
    val isDark = isDark(LocalTheme.current)
    MaterialTheme(
        colorScheme = colorScheme(isDark),
        typography = typography(),
        content = content,
    )
}

@Composable
internal fun isDark(theme: Theme) = when (theme) {
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
