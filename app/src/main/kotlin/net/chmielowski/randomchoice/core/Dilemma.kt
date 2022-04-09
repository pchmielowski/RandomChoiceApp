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
import net.chmielowski.randomchoice.core.Option.Image
import net.chmielowski.randomchoice.core.Option.Text
import net.chmielowski.randomchoice.utils.AndroidString
import net.chmielowski.randomchoice.utils.removeIndex
import net.chmielowski.randomchoice.utils.replace

@Parcelize
internal data class Dilemma(private val options: List<Option> = listOf(Text(), Text())) :
    Parcelable {

    init {
        check(options.size >= 2)
        check(options.all { it is Text } || options.all { it is Image })
    }

    val mode
        get() = when (options.first()) {
            is Text -> Mode.Text
            is Image -> Mode.Image
        }

    val canRemove get() = options.size > 2

    val canResetOrSave get() = options.any(Option::hasValue)

    fun updateText(id: OptionId, text: Option) = Dilemma(options.replace(id.value, text))

    fun reset() = Dilemma()

    val allFilled get() = options.all(Option::hasValue)

    fun addNew() = Dilemma(options + Text())

    // TODO: Rename
    fun addShared(option: Option) = copy(
        options = options.replaceFirstEmptyOrAdd(option),
    )

    private fun List<Option>.replaceFirstEmptyOrAdd(option: Option) =
        when (val empty = indexOfFirst { it == Text() }) {
            -1 -> this + option
            else -> replace(empty, option)
        }

    fun remove(id: OptionId): Dilemma {
        if (!canRemove) {
            // Happen when user clicks REMOVE button before they are hidden.
            return this
        }
        return Dilemma(options.removeIndex(id.value))
    }

    fun choose(choice: Choice) = Result(options, choice.make(options))

    fun selectMode(mode: Mode) = when (mode) {
        Mode.Text -> Dilemma()
        Mode.Image -> Dilemma(listOf(Image(), Image()))
    }

    fun persistable() = options

    @Composable
    fun summary() = buildAnnotatedString {
        for ((index, item) in options.withIndex()) {
            if (item == Text()) {
                continue
            }
            val text = when (item) {
                is Text -> item.text
                is Image -> throw SavingImagesNotSupportedException()
            }
            append(text)
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

    fun render() = options.mapIndexed(::renderOption)

    private fun renderOption(index: Int, item: Option): OptionField {
        val id = OptionId(index)
        val label = AndroidString(R.string.label_option, index + 1)
        return when (item) {
            is Text -> textField(id, index, item, label)
            is Image -> ImageField(id, item, label)
        }
    }

    private fun textField(id: OptionId, index: Int, item: Text, label: AndroidString) = TextField(
        id = id,
        value = item,
        imeAction = if (index == options.lastIndex) {
            ImeAction.Done
        } else {
            ImeAction.Next
        },
        focused = index == 0,
        humanIndex = index + 1,
        isLast = index == options.lastIndex,
        label = label,
    )

    sealed interface OptionField {

        val id: OptionId

        val label: AndroidString
    }

    data class TextField(
        override val id: OptionId,
        val value: Text,
        val imeAction: ImeAction,
        val focused: Boolean,
        val humanIndex: Int,
        val isLast: Boolean,
        override val label: AndroidString,
    ) : OptionField

    data class ImageField(
        override val id: OptionId,
        val value: Image,
        override val label: AndroidString,
    ) : OptionField

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
