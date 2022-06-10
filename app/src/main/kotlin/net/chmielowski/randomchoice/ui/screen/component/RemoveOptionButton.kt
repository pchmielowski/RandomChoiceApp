@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import net.chmielowski.randomchoice.R

@Composable
internal fun RemoveOptionButton(
    onClick: () -> Unit,
    index: Int,
    modifier: Modifier = Modifier,
    canRemove: Boolean,
) {
    AnimatedVisibility(canRemove, modifier = modifier) {
        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.Remove,
                contentDescription = stringResource(R.string.action_remove_option, index),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
