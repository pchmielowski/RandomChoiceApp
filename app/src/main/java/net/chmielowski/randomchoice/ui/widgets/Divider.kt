@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.widgets

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun Divider(modifier: Modifier = Modifier) {
    // Alpha value copied from Material 2. Refactor after Material 3 Divider is updated to use alpha.
    androidx.compose.material3.Divider(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        modifier = modifier,
    )
}
