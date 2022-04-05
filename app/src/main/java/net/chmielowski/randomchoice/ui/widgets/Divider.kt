@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun Divider(modifier: Modifier = Modifier) {
    // Current implementation of Material 3 divider does not use alpha.
    androidx.compose.material.Divider(modifier = modifier)
}
