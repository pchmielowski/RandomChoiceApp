@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.saved

import android.annotation.SuppressLint
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.map
import net.chmielowski.randomchoice.R
import net.chmielowski.randomchoice.core.Dilemma
import net.chmielowski.randomchoice.core.DilemmaId
import net.chmielowski.randomchoice.core.Intent
import net.chmielowski.randomchoice.core.Intent.DilemmaIntent
import net.chmielowski.randomchoice.persistence.ObserveSavedDilemmas
import net.chmielowski.randomchoice.ui.widgets.Scaffold
import net.chmielowski.randomchoice.utils.Loadable

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
internal fun SavedScreen(
    navigator: DestinationsNavigator,
    observeSavedDilemmas: ObserveSavedDilemmas,
    onIntent: (Intent) -> Unit,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberSplineBasedDecay())
    Scaffold(
        navigateUp = navigator::navigateUp,
        title = stringResource(R.string.label_saved),
        scrollBehavior = scrollBehavior,
    ) {
        @SuppressLint("FlowOperatorInvokedInComposition")
        val loadable by observeSavedDilemmas()
            .map { Loadable.Loaded(it) }
            .collectAsState(Loadable.Loading)

        @Suppress("UnnecessaryVariable")
        val current = loadable
        if (current is Loadable.Loaded) {
            val items = current.content
            if (items.isEmpty()) {
                EmptyView()
            } else {
                ItemList(
                    items = items,
                    onIntent = onIntent,
                    navigator = navigator,
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemList(
    items: List<Pair<DilemmaId, Dilemma>>,
    onIntent: (Intent) -> Unit,
    navigator: DestinationsNavigator,
    modifier: Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {
        items(items, { (id, _) -> id }) { (id, options) ->
            SavedChoiceItem(
                dilemma = options,
                onDelete = { onIntent(DilemmaIntent.Delete(id)) },
                onReuse = {
                    onIntent(DilemmaIntent.Reuse(options))
                    navigator.navigateUp()
                },
                modifier = Modifier.animateItemPlacement(),
            )
        }
    }
}

@Composable
private fun EmptyView() {
    Text(
        text = stringResource(R.string.message_empty),
        modifier = Modifier.padding(16.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SavedChoiceItem(
    dilemma: Dilemma,
    onDelete: () -> Unit,
    onReuse: () -> Unit,
    modifier: Modifier,
) {
    OutlinedCard(modifier = modifier) {
        Text(
            text = dilemma.summary(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        Divider()
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .align(Alignment.End),
        ) {
            TextButton(onClick = onDelete) {
                Icon(Icons.Outlined.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.action_delete))
            }
            OutlinedButton(onClick = onReuse) {
                Icon(Icons.Outlined.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.action_reuse))
            }
        }
    }
}
