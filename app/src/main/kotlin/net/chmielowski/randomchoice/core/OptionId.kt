package net.chmielowski.randomchoice.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@JvmInline
@Parcelize
value class OptionId(val value: Int) : Parcelable
