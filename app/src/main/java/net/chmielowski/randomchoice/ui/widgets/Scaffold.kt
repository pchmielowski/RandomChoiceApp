@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.widgets

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import net.chmielowski.randomchoice.ui.screen.component.BackButton
import net.chmielowski.randomchoice.ui.screen.component.TopBar

@Suppress("LongParameterList")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Scaffold(
    navigateUp: (() -> Unit)? = null,
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    floatingActionButton: @Composable () -> Unit = { },
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    snackbarHostState: SnackbarHostState? = null,
    background: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    androidx.compose.material3.Scaffold(
        topBar = {
            TopBar(
                navigationIcon = {
                    if (navigateUp != null) {
                        BackButton(navigateUp)
                    }
                },
                title = title,
                actions = actions,
            )
        },
        floatingActionButton = floatingActionButton,
        snackbarHost = {
            if (snackbarHostState != null) {
                SnackbarHost(snackbarHostState)
            }
        },
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsWithImePadding()
    ) {
        // Using legacy Material2 just for the correct colors on Material2 widgets.
        androidx.compose.material.Scaffold(backgroundColor = backgroundColor) {
            background()
            Divider()
            content()
        }
    }
}
