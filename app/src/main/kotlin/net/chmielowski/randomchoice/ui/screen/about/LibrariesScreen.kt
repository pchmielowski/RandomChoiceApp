@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import net.chmielowski.randomchoice.R
import net.chmielowski.randomchoice.ui.screen.destinations.LicenseScreenDestination
import net.chmielowski.randomchoice.ui.widgets.Divider
import net.chmielowski.randomchoice.ui.widgets.Scaffold
import net.chmielowski.randomchoice.ui.widgets.rememberScrollBehavior

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Destination
@Composable
internal fun LibrariesScreen(navigator: DestinationsNavigator = EmptyDestinationsNavigator) {
    val scrollBehavior = rememberScrollBehavior()
    Scaffold(
        navigateUp = navigator::navigateUp,
        title = stringResource(R.string.label_libraries),
        scrollBehavior = scrollBehavior,
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.label_app_uses_following_libraries),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxWidth(),
            ) {
                items(libraries, key = Library::name) { library ->
                    LibraryItem(
                        navigator = navigator,
                        library = library,
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryItem(
    navigator: DestinationsNavigator,
    library: Library,
) {
    Column {
        Text(library.name, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            library.url, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WebPageButton(library = library)
            LicenseButton(
                navigator = navigator,
                library = library,
            )
        }
    }
}

@Composable
private fun WebPageButton(library: Library) {
    val context = LocalContext.current
    ElevatedButton(onClick = { context.openWebPage(library) }) {
        Text(stringResource(R.string.action_open_webpage))
    }
}

private fun Context.openWebPage(library: Library) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(library.url)))
}

@Composable
private fun LicenseButton(
    navigator: DestinationsNavigator,
    library: Library
) {
    TextButton(onClick = { navigator.navigateToLicense(library) }) {
        Text(stringResource(R.string.action_show_license))
    }
}

private fun DestinationsNavigator.navigateToLicense(library: Library) {
    navigate(LicenseScreenDestination(library.name))
}
