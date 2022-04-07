package net.chmielowski.randomchoice.ui.theme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

internal interface ThemePreference {

    val current: Theme

    fun write(theme: Theme)
}

internal class ThemePreferenceImpl(context: Context) : ThemePreference {

    private val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)

    private val key = "Theme"

    override var current by mutableStateOf(read())
        private set

    private fun read(): Theme {
        val encoded = preferences.getInt(key, Theme.System.encoded)
        return Theme.values().single { it.encoded == encoded }
    }

    override fun write(theme: Theme) {
        preferences
            .edit()
            .putInt(key, theme.encoded)
            .apply()
        current = theme
    }
}

internal enum class Theme(val encoded: Int) {
    System(0),
    Light(1),
    Dark(2),
}
