package net.chmielowski.randomchoice.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@JvmInline
@Parcelize
value class Option(val text: String = "") : Parcelable
