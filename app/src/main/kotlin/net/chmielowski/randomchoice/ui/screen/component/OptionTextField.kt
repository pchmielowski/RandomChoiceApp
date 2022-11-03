@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
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
            placeholder = { Text(stringResource(label)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = imeAction,
            ),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            trailingIcon = {
                if (value.hasValue) {
                    ClearOptionButton(onValueChange)
                }
            },
            modifier = modifier
                .weight(1F)
                .animateFirstAppearance()
        )
        RemoveOptionButton(
            onClick = onRemoveOption,
            index = index,
            canRemove = canRemove,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun ClearOptionButton(onValueChange: (Option.Text) -> Unit) {
    IconButton(onClick = { onValueChange(Option.Text("")) }) {
        Icon(
            Icons.Default.Clear,
            contentDescription = stringResource(R.string.action_clear),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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
