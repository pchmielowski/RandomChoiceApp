@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import net.chmielowski.randomchoice.R
import net.chmielowski.randomchoice.core.Option
import net.chmielowski.randomchoice.utils.AndroidString
import net.chmielowski.randomchoice.utils.stringResource

@Suppress("LongParameterList")
@Composable
internal fun OptionTextField(
    value: Option.Text,
    onValueChange: (Option.Text) -> Unit,
    onRemoveOption: () -> Unit,
    imeAction: ImeAction,
    index: Int,
    canRemove: Boolean,
    modifier: Modifier = Modifier,
    label: AndroidString,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth(),
    ) {
        TextField(
            value = value.text,
            onValueChange = { onValueChange(Option.Text(it)) },
            placeholder = {
                Text(
                    text = stringResource(label),
                    color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium),
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = imeAction,
            ),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            trailingIcon = {
                if (value.hasValue) {
                    IconButton(onClick = { onValueChange(Option.Text("")) }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(R.string.action_clear)
                        )
                    }
                }
            },
            modifier = modifier
                .weight(1F)
                .animateFirstAppearance()
        )
        AnimatedVisibility(canRemove) { // TODO: Overshoot interpolator.
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onRemoveOption) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = stringResource(R.string.action_remove_option, index),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

private fun Modifier.animateFirstAppearance() = composed {
    val scale = rememberSaveable(saver = animatableSaver()) { Animatable(0F) }
    LaunchedEffect(Unit) {
        scale.animateTo(1F, spring())
    }
    Modifier.scale(scale.value)
}

private fun animatableSaver() = object : Saver<Animatable<Float, AnimationVector1D>, Float> {

    override fun restore(value: Float) = Animatable(value)

    override fun SaverScope.save(value: Animatable<Float, AnimationVector1D>) = value.value
}
