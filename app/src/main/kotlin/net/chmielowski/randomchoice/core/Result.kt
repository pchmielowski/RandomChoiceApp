package net.chmielowski.randomchoice.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(val options: List<Option>, val chosenIndex: Int) : Parcelable {

    val chosen get() = options[chosenIndex]
}
