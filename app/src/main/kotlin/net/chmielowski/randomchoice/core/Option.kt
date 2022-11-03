package net.chmielowski.randomchoice.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

sealed interface Option : Parcelable {

    val hasValue: Boolean

    @JvmInline
    @Parcelize
    value class Text(val text: String = "") : Option {

        override val hasValue get() = text.isNotEmpty()
    }

    @JvmInline
    @Parcelize
    value class Image(val file: File? = null) : Option {

        override val hasValue get() = file != null
    }
}
