@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import net.chmielowski.randomchoice.core.Result

@Composable
internal fun AnimatedResult(
    result: Result,
    offset: Float,
    finished: Boolean,
    modifier: Modifier,
    textStyle: TextStyle
) {
    val items = result.options
    Layout(
        content = {
            ProvideTextStyle(textStyle) {
                for (item in (items + items.first())) {
                    Text(
                        text = item.text,
                        maxLines = if (finished) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
        modifier = modifier.clipToBounds()
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val width = placeables.maxOf(Placeable::width)
        val height = placeables.maxOf(Placeable::height)
        layout(width, height) {
            for ((index, placeable) in placeables.withIndex()) {
                val y = height * (index - offset * (placeables.size - 1))
                placeable.place(0, y.toInt())
            }
        }
    }
}

@Composable
internal fun offset(chosenItemIndex: Int, itemsCount: Int): Pair<Float, Boolean> {
    var target by rememberSaveable { mutableStateOf(0F) }

    @Suppress("MagicNumber")
    val repeats = 10
    SideEffect { target = repeats + chosenItemIndex.toFloat() / itemsCount }
    var finished by rememberSaveable { mutableStateOf(false) }
    val progress by animateFloatAsState(target, spec()) { finished = true }
    return progress.mod(1F) to finished
}

@Suppress("MagicNumber")
private fun spec() = tween<Float>(1_500, easing = LinearOutSlowInEasing)
