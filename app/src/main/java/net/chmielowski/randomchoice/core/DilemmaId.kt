package net.chmielowski.randomchoice.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@JvmInline
value class DilemmaId(val value: Long) : Parcelable
