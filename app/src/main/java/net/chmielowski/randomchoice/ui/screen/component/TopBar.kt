@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
internal fun TopBar(
    navigationIcon: @Composable () -> Unit = {},
    title: String,
    actions: @Composable() (RowScope.() -> Unit) = {},
) {
    LargeTopAppBar(
        navigationIcon = navigationIcon,
        title = { Text(title) },
        actions = actions
    )
}
