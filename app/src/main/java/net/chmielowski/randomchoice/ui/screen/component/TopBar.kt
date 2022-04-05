@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding

@Composable
internal fun TopBar(
    navigationIcon: @Composable () -> Unit = {},
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior?,
) {
    LargeTopAppBar(
        navigationIcon = navigationIcon,
        title = { Text(title) },
        actions = actions,
        scrollBehavior = scrollBehavior,
        modifier = Modifier
            .statusBarsPadding(), // TODO@ Problem is here. We need contentPadding instead of padding.
    )
}
