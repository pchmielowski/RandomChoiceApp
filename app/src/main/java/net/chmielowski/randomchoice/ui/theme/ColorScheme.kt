package net.chmielowski.randomchoice.ui.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import net.chmielowski.randomchoice.R

@SuppressLint("NewApi")
@Composable
internal fun colorScheme(darkTheme: Boolean): ColorScheme {
    val context = LocalContext.current

    @Suppress("MagicNumber")
    val supportsDynamicColors = Build.VERSION.SDK_INT >= 31
    return if (darkTheme) {
        if (supportsDynamicColors) {
            dynamicDarkColorScheme(context)
        } else {
            appDarkColorScheme()
        }
    } else {
        if (supportsDynamicColors) {
            dynamicLightColorScheme(context)
        } else {
            appLightColorScheme()
        }
    }
}

@Suppress("MagicNumber")
private fun appDarkColorScheme() = darkColorScheme(
    primary = Color(0xFF76D1FF),
    onPrimary = Color(0xFF003549),
    primaryContainer = Color(0xFF004C69),
    onPrimaryContainer = Color(0xFFC1E8FF),
    inversePrimary = Color(0xFF00668B),
    secondary = Color(0xFFB5CAD7),
    onSecondary = Color(0xFF20333D),
    secondaryContainer = Color(0xFF374955),
    onSecondaryContainer = Color(0xFFD1E5F4),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF484264),
    onTertiaryContainer = Color(0xFFE6DEFF),
    background = Color(0xFF191C1E),
    onBackground = Color(0xFFE1E3E5),
    surface = Color(0xFF191C1E),
    onSurface = Color(0xFFE1E3E5),
    surfaceVariant = Color(0xFF40484D),
    onSurfaceVariant = Color(0xFFC0C7CD),
    inverseSurface = Color(0xFFE1E3E5),
    inverseOnSurface = Color(0xFF2E3133),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF2B8B5),
    outline = Color(0xFF8A9297),
)

@Suppress("MagicNumber")
@Composable
private fun appLightColorScheme() = lightColorScheme(
    primary = colorResource(R.color.primary),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC1E8FF),
    onPrimaryContainer = Color(0xFF001E2C),
    inversePrimary = Color(0xFF76D1FF),
    secondary = Color(0xFF4E616C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD1E5F4),
    onSecondaryContainer = Color(0xFF091E28),
    tertiary = Color(0xFF7D5260),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE6DEFF),
    onTertiaryContainer = Color(0xFF1D1736),
    background = colorResource(R.color.background),
    onBackground = Color(0xFF191C1E),
    surface = Color(0xFFFCFCFF),
    onSurface = Color(0xFF191C1E),
    surfaceVariant = Color(0xFFDCE3E9),
    onSurfaceVariant = Color(0xFF40484D),
    inverseSurface = Color(0xFF2E3133),
    inverseOnSurface = Color(0xFFF0F0F3),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    outline = Color(0xFF70777C),
)
