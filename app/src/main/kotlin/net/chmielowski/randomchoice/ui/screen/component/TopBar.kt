@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBar(
    navigationIcon: @Composable () -> Unit = {},
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior?,
) = LargeTopAppBar(
    navigationIcon = navigationIcon,
    title = { Text(title) },
    actions = actions,
    scrollBehavior = scrollBehavior,
)
