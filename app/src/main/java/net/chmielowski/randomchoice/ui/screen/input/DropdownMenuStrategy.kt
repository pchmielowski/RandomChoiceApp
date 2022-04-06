@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.input

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

internal interface DropdownMenuStrategy {

    @Composable
    fun Container(content: @Composable () -> Unit)

    @Composable
    fun Menu(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        content: @Composable () -> Unit,
    )

    @Composable
    fun Item(
        icon: ImageVector,
        choice: Boolean?,
        @StringRes text: Int,
        onClick: () -> Unit,
        onDismiss: () -> Unit,
    )

    @Composable
    fun Divider(modifier: Modifier)

    class Real : DropdownMenuStrategy {

        @Composable
        override fun Container(content: @Composable () -> Unit) = Box(content = { content() })

        @Composable
        override fun Menu(
            expanded: Boolean,
            onDismissRequest: () -> Unit,
            content: @Composable () -> Unit
        ) =
            androidx.compose.material.DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
            ) {
                content()
            }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        override fun Item(
            icon: ImageVector,
            choice: Boolean?,
            @StringRes text: Int,
            onClick: () -> Unit,
            onDismiss: () -> Unit,
        ) {
            DropdownMenuItem(
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
        }

        @Composable
        override fun Divider(modifier: Modifier) = MenuDefaults.Divider(modifier = modifier)
    }
}
