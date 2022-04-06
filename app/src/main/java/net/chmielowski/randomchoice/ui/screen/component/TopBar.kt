@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding

@Composable
internal fun TopBar(
    navigationIcon: @Composable () -> Unit = {},
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior?,
) {
    val colors = TopAppBarDefaults.largeTopAppBarColors()
    val color by colors.containerColor(scrollBehavior?.scrollFraction ?: 0F)
    Surface(color = color, modifier = Modifier) {
        LargeTopAppBar(
            navigationIcon = navigationIcon,
            title = { Text(title) },
            actions = actions,
            scrollBehavior = scrollBehavior,
            colors = colors,
            modifier = Modifier
                .statusBarsPadding(),
        )
    }
}
