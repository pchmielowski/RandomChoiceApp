@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.navigationBarsWithImePadding
import net.chmielowski.randomchoice.ui.screen.component.BackButton
import net.chmielowski.randomchoice.ui.screen.component.TopBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Suppress("LongParameterList")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Scaffold(
    navigateUp: (() -> Unit)? = null,
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    floatingActionButton: @Composable () -> Unit = { },
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    scrollBehavior: TopAppBarScrollBehavior? = null,
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
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = floatingActionButton,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .navigationBarsWithImePadding()
    ) { padding ->
        // Using legacy Material2 just for the correct colors on Material2 widgets.
        androidx.compose.material.Scaffold(backgroundColor = backgroundColor, modifier = Modifier.padding(padding)) {
            background()
            content()
            Divider(modifier = Modifier.alpha(1F - (scrollBehavior?.state?.collapsedFraction ?: 0F)))
        }
    }
}
