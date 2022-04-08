package net.chmielowski.randomchoice

import com.arkivanov.mvikotlin.core.store.Store
import net.chmielowski.randomchoice.core.Intent
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent
import net.chmielowski.randomchoice.core.Option

internal class ShareIntentDelegate(private val store: Store<Intent, *, *>) {

    fun onCreate(intent: android.content.Intent) = handleIntent(intent)

    fun onNewIntent(intent: android.content.Intent?) = handleIntent(intent)

    private fun handleIntent(intent: android.content.Intent?) {
        val option = intent?.sharedOption
        if (option != null) {
            store.accept(EnterOptionsIntent.Add(option))
        }
    }

    private val android.content.Intent.sharedOption: Option?
        get() {
            val extras = extras ?: return null
            val text = (extras.getString(android.content.Intent.EXTRA_SUBJECT)
                ?: extras.getString(android.content.Intent.EXTRA_TEXT))
                ?: return null
            return Option.Text(text)
        }
}
