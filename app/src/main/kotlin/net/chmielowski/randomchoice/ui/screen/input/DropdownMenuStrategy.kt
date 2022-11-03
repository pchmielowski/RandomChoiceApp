@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.input

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.Divider as MaterialDivider

internal class DropdownMenuStrategy {

    @Composable
    fun Container(content: @Composable () -> Unit) = Box(content = { content() })

    @Composable
    fun Menu(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        content: @Composable () -> Unit
    ) = DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        content()
    }

    @Suppress("ComposableNaming")
    @Composable
    fun Item(
        icon: ImageVector,
        choice: Boolean?,
        @StringRes text: Int,
        onClick: () -> Unit,
        onDismiss: () -> Unit,
    ) = DropdownMenuItem(
        onClick = {
            onClick()
            onDismiss()
        },
        leadingIcon = { Icon(icon, contentDescription = null) },
        trailingIcon = if (choice != null) {
            { RadioButton(choice, { onClick(); onDismiss() }) }
        } else {
            null
        },
        text = { Text(stringResource(text)) }
    )

    @Composable
    fun Divider(modifier: Modifier) = MaterialDivider(modifier = modifier)
}
