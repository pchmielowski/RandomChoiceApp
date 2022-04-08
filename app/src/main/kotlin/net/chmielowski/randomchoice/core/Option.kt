package net.chmielowski.randomchoice.core

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface Option : Parcelable {

    val hasValue: Boolean

    @JvmInline
    @Parcelize
    value class Text(val text: String = "") : Option {

        override val hasValue get() = text.isNotEmpty()
    }

    @JvmInline
    @Parcelize
    value class Image(val bitmap: Bitmap? = null) : Option {

        override val hasValue get() = bitmap != null
    }
}
