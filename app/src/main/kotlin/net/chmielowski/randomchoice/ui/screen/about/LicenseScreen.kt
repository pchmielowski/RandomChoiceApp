@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.about

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import net.chmielowski.randomchoice.ui.widgets.Scaffold
import net.chmielowski.randomchoice.ui.widgets.rememberScrollBehavior

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
internal fun LicenseScreen(navigator: DestinationsNavigator, libraryName: String) {
    val scrollBehavior = rememberScrollBehavior()
    Scaffold(
        navigateUp = navigator::navigateUp,
        title = libraryName,
        scrollBehavior = scrollBehavior,
    ) {
        Text(
            text = libraries.single { library -> library.name == libraryName }.license,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        )
    }
}
