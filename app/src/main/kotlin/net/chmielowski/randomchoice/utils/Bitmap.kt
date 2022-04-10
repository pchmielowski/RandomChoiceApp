package net.chmielowski.randomchoice.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

internal fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 70, stream)
    return stream.toByteArray()
}

internal fun ByteArray.toBitmap() = BitmapFactory.decodeByteArray(this, 0, size)