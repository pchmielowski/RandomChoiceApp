package net.chmielowski.randomchoice.core

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface Option : Parcelable {

    @JvmInline
    @Parcelize
    value class Text(val text: String = "") : Option

    @JvmInline
    @Parcelize
    value class Image(val bitmap: Bitmap) : Option
}
