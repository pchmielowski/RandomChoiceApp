package net.chmielowski.randomchoice.utils

import androidx.annotation.StringRes

data class AndroidString(
    @StringRes val id: Int,
    val args: List<Any>,
) {

    constructor(id: Int, vararg args: Any) : this(id, args.toList())
}
