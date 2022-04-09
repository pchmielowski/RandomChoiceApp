package net.chmielowski.randomchoice.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

internal data class AndroidString(
    @StringRes val id: Int,
    val args: List<Any>,
) {

    constructor(id: Int, vararg args: Any) : this(id, args.toList())
}

@Suppress("SpreadOperator")
@Composable
@ReadOnlyComposable
internal fun stringResource(string: AndroidString) =
    stringResource(string.id, *string.args.toTypedArray())
