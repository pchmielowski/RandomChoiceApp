package net.chmielowski.randomchoice.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// TODO@ String -> Option
@Parcelize
data class Result(val options: List<String>, val chosenIndex: Int) : Parcelable {

    val chosen get() = options[chosenIndex]
}
