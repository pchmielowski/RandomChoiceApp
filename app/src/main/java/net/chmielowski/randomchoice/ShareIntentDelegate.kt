package net.chmielowski.randomchoice

import com.arkivanov.mvikotlin.core.store.Store
import net.chmielowski.randomchoice.core.Intent
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent

internal class ShareIntentDelegate(private val store: Store<Intent, *, *>) {

    fun onCreate(intent: android.content.Intent) {
        val sharedText = intent.sharedText
        if (sharedText != null) {
            store.accept(EnterOptionsIntent.Add(sharedText))
        }
    }

    fun onNewIntent(intent: android.content.Intent?) {
        val sharedText = intent?.sharedText
        if (sharedText != null) {
            store.accept(EnterOptionsIntent.Add(sharedText))
        }
    }

    private val android.content.Intent.sharedText: String?
        get() {
            val extras = extras ?: return null
            return extras.getString(android.content.Intent.EXTRA_SUBJECT)
                ?: extras.getString(android.content.Intent.EXTRA_TEXT)
        }
}
