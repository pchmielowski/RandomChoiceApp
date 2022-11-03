package net.chmielowski.randomchoice.utils

import android.os.Build
import android.os.Bundle

@Suppress("MagicNumber")
internal inline fun <reified T> Bundle.getParcelableCompat(key: String) =
    if (Build.VERSION.SDK_INT >= 33) {
        getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION") getParcelable(key)
    }
