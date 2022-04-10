package net.chmielowski.randomchoice.core

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.chmielowski.randomchoice.utils.toBitmap
import net.chmielowski.randomchoice.utils.toByteArray

sealed interface Option : Parcelable {

    val hasValue: Boolean

    @JvmInline
    @Parcelize
    value class Text(val text: String = "") : Option {

        override val hasValue get() = text.isNotEmpty()
    }

    @JvmInline
    @Parcelize
    value class Image(private val array: ByteArray? = null) : Option {

        constructor(bitmap: Bitmap?) : this(bitmap?.toByteArray())

        val bitmap get() = array?.toBitmap()

        override val hasValue get() = array != null
    }
}
