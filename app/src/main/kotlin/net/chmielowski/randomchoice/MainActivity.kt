package net.chmielowski.randomchoice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.arkivanov.mvikotlin.core.store.Store
import net.chmielowski.randomchoice.core.Intent
import net.chmielowski.randomchoice.core.Label
import net.chmielowski.randomchoice.core.State
import net.chmielowski.randomchoice.core.createStateStore
import net.chmielowski.randomchoice.ui.Content
import net.chmielowski.randomchoice.ui.screen.input.DropdownMenuStrategy
import net.chmielowski.randomchoice.utils.getParcelableCompat
import org.koin.android.ext.android.get
import java.io.File

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

//            val file = File(
//                "/storage/emulated/0/Android/data/net.chmielowski.randomchoice/files/Pictures/Random Choice7504732900224082083.jpg"
//            )
//            val painter = rememberAsyncImagePainter(file)
//            Column {
//                val state = painter.state
//                if (state is AsyncImagePainter.State.Error) {
//                    val txt = state.result.throwable.toString()
//                    Text(txt)
//                }
//                Image(
//                    painter,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .background(Color.Red)
//                        .fillMaxSize(0.8F),
//                )
//            }
        }
    }

    private fun getOrCreateStore(): Store<Intent, State, Label> {
        val registryKey = "registry"
        val valueKey = "state"
        val state = savedStateRegistry
            .consumeRestoredStateForKey(registryKey)
            ?.getParcelableCompat<State>(valueKey)
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
