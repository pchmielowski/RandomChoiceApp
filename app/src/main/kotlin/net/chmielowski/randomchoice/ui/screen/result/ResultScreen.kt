@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import net.chmielowski.randomchoice.R
import net.chmielowski.randomchoice.core.Option
import net.chmielowski.randomchoice.core.Result
import net.chmielowski.randomchoice.ui.screen.component.AnimatedResult
import net.chmielowski.randomchoice.ui.screen.component.offset
import net.chmielowski.randomchoice.ui.widgets.Scaffold

@Destination
@Composable
internal fun ResultScreen(
    navigator: DestinationsNavigator,
    result: Result,
) {
    val (offset, finished) = offset(result.chosenIndex, result.options.count())
    Scaffold(
        navigateUp = navigator::navigateUp,
        title = title(finished),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val description = stringResource(R.string.label_chosen_option)
            Text(
                description,
                style = MaterialTheme.typography.labelLarge
            )
            AnimatedResult(
                result = result,
                offset = offset,
                finished = finished,
                textStyle = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .clearAndSetSemantics {
                        contentDescription = description
                        text = AnnotatedString(
                            when (val chosen = result.chosen) {
                                is Option.Text -> chosen.text
                                is Option.Image -> "" // TODO@
                            }
                        )
                    }
            )
        }
    }
}

@Composable
private fun title(finished: Boolean) =
    stringResource(if (finished) R.string.message_done else R.string.message_throwing_dice)
