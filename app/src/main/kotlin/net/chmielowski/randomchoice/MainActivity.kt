package net.chmielowski.randomchoice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import com.arkivanov.mvikotlin.core.store.Store
import net.chmielowski.randomchoice.core.Intent
import net.chmielowski.randomchoice.core.Label
import net.chmielowski.randomchoice.core.State
import net.chmielowski.randomchoice.core.createStateStore
import net.chmielowski.randomchoice.ui.Content
import net.chmielowski.randomchoice.ui.screen.input.DropdownMenuStrategy
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    private val stateStore by lazy(LazyThreadSafetyMode.NONE) { getOrCreateStore() }

    private val intentDelegate by lazy(LazyThreadSafetyMode.NONE) { ShareIntentDelegate(stateStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        intentDelegate.onCreate(intent)
        setContent {
            Content(
                preference = get(),
                observeSavedDilemmas = get(),
                store = stateStore,
                menuStrategy = DropdownMenuStrategy.Real(),
            )
        }
    }

    private fun getOrCreateStore(): Store<Intent, State, Label> {
        val registryKey = "registry"
        val valueKey = "state"
        val state = savedStateRegistry
            .consumeRestoredStateForKey(registryKey)
            ?.getParcelable(valueKey, State::class.java)
        savedStateRegistry.registerSavedStateProvider(registryKey) {
            bundleOf(valueKey to stateStore.state)
        }
        return createStateStore(get(), state ?: State())
    }

    override fun onNewIntent(intent: android.content.Intent?) {
        super.onNewIntent(intent)
        intentDelegate.onNewIntent(intent)
    }
}
