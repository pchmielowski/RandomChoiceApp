@file:Suppress("FunctionName", "TooManyFunctions")

package net.chmielowski.randomchoice.core

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import kotlinx.parcelize.Parcelize
import net.chmielowski.randomchoice.R
import net.chmielowski.randomchoice.utils.removeIndex
import net.chmielowski.randomchoice.utils.replace

@Parcelize
internal data class Dilemma(private val options: List<Option> = listOf(Option(), Option())) :
    Parcelable {

    init {
        check(options.size >= 2)
    }

    val canRemove get() = options.size > 2

    val canResetOrSave get() = options.any { it != Option() }

    fun updateText(id: Int, text: String) = Dilemma(options.replace(id, Option(text)))

    fun reset() = Dilemma()

    val allFilled get() = options.all { it != Option() }

    fun addNew() = Dilemma(options + Option())

    // TODO: Rename
    fun addShared(text: String) = copy(
        options = options.replaceFirstEmptyOrAdd(text),
    )

    private fun List<Option>.replaceFirstEmptyOrAdd(text: String) =
        when (val empty = indexOfFirst { it == Option() }) {
            -1 -> this + Option(text)
            else -> replace(empty, Option(text))
        }

    fun remove(id: Int): Dilemma {
        if (!canRemove) {
            // Happen when user clicks REMOVE button before they are hidden.
            return this
        }
        return Dilemma(options.removeIndex(id))
    }

    fun choose(choice: Choice) = Result(options.map(Option::text), choice.make(options))

    fun persistable() = options

    @Composable
    fun summary() = buildAnnotatedString {
        for ((index, item) in options.withIndex()) {
            if (item == Option()) {
                continue
            }
            append(item.text)
            if (index != options.lastIndex) {
                appendSeparator()
            }
        }
    }

    @SuppressLint("ComposableNaming")
    @Composable
    private fun AnnotatedString.Builder.appendSeparator() {
        append(" ")
        withStyle(SpanStyle(LocalContentColor.current.copy(alpha = ContentAlpha.medium))) {
            append(stringResource(R.string.label_or))
        }
        append(" ")
    }

    fun render() = options.mapIndexed(::textField)

    private fun textField(index: Int, item: Option) = TextField(
        value = item.text,
        imeAction = if (index == options.lastIndex) {
            ImeAction.Done
        } else {
            ImeAction.Next
        },
        focused = index == 0,
        humanIndex = index + 1,
        id = index,
        isLast = index == options.lastIndex,
    )

    data class TextField(
        val value: String,
        val imeAction: ImeAction,
        val focused: Boolean,
        val id: Int,
        val humanIndex: Int,
        val isLast: Boolean,
    )

    @Composable
    fun LaunchWhenOptionAdded(block: suspend () -> Unit) {
        var previous by remember { mutableStateOf(options.size) }
        val current = options.size
        LaunchedEffect(current) {
            if (current == previous + 1) {
                block()
            }
            previous = current
        }
    }
}
