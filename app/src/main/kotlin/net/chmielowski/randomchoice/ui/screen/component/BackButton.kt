@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import net.chmielowski.randomchoice.R

@Composable
internal fun BackButton(navigateUp: () -> Unit) {
    IconButton(onClick = navigateUp) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(R.string.action_navigate_up),
        )
    }
}
