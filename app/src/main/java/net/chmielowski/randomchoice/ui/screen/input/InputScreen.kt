@file:Suppress("FunctionName", "TooManyFunctions")

package net.chmielowski.randomchoice.ui.screen.input

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.chmielowski.randomchoice.R
import net.chmielowski.randomchoice.core.Dilemma
import net.chmielowski.randomchoice.core.Intent
import net.chmielowski.randomchoice.core.Intent.DilemmaIntent
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent
import net.chmielowski.randomchoice.core.Label
import net.chmielowski.randomchoice.core.Label.FocusFirstOptionInput
import net.chmielowski.randomchoice.core.Label.ShowResult
import net.chmielowski.randomchoice.core.State
import net.chmielowski.randomchoice.ui.CircularRevealAnimation
import net.chmielowski.randomchoice.ui.destinations.AboutScreenDestination
import net.chmielowski.randomchoice.ui.destinations.ResultScreenDestination
import net.chmielowski.randomchoice.ui.destinations.SavedScreenDestination
import net.chmielowski.randomchoice.ui.screen.component.OptionTextField
import net.chmielowski.randomchoice.ui.theme.LocalTheme
import net.chmielowski.randomchoice.ui.theme.Theme
import net.chmielowski.randomchoice.ui.widgets.Scaffold
import net.chmielowski.randomchoice.utils.Observe

@OptIn(ExperimentalCoroutinesApi::class)
@Destination(start = true)
@Composable
internal fun InputScreen(
    navigator: DestinationsNavigator,
    store: Store<Intent, State, Label>,
) {
    val state by store.states.collectAsState(State())
    val focusRequester = remember { FocusRequester() }
    store.labels.Observe { label ->
        when (label) {
            is ShowResult -> navigator.navigate(ResultScreenDestination(label.result))
            FocusFirstOptionInput -> focusRequester.requestFocus()
        }
    }
    InputScreen(
        navigator = navigator,
        state = state,
        onIntent = store::accept,
        focusRequester = focusRequester,
    )
}

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun InputScreen(
    navigator: DestinationsNavigator,
    state: State,
    onIntent: (Intent) -> Unit,
    focusRequester: FocusRequester,
) {
    var transitionVisible by remember { mutableStateOf(false) }
    Scaffold(
        title = stringResource(R.string.label_enter_options),
        actions = {
            if (state.showResetButton) {
                ResetButton(onIntent)
            }
            if (state.showSaveButton) {
                SaveButton(onIntent)
            }
            if (state.showSavedMessage) {
                SavedMessage()
            }
            MenuButton(
                onThemeChoose = { theme -> onIntent(Intent.SetTheme(theme)) },
                onAboutClick = { navigator.navigate(AboutScreenDestination) },
                onShowSavedClick = { navigator.navigate(SavedScreenDestination) },
            )
        },
        floatingActionButton = {
            if (state.dilemma.allFilled) {
                val keyboardController = LocalSoftwareKeyboardController.current
                MakeChoiceButton(onActionClick = {
                    keyboardController?.hide()
                    transitionVisible = true
                })
            }
        },
        background = {
            if (transitionVisible) {
                CircularRevealAnimation(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    onEnd = { onIntent(Intent.MakeChoice) },
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            OptionTextFields(
                dilemma = state.dilemma,
                onIntent = onIntent,
                focusRequester = focusRequester,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                PasteButton(onIntent = onIntent)
                Spacer(modifier = Modifier.width(8.dp))
                AddOptionButton(
                    onIntent = onIntent,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(100.dp)) // Let the user scroll content up.
        }
    }
}

@Composable
internal fun MenuButton(
    onThemeChoose: (Theme) -> Unit,
    onAboutClick: () -> Unit,
    onShowSavedClick: () -> Unit,
) {
//    Box {
        var expanded by remember { mutableStateOf(false) }
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.label_more)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismiss = { expanded = false },
            onThemeChoose = onThemeChoose,
            onAboutClick = onAboutClick,
            onShowSavedClick = onShowSavedClick,
        )
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onThemeChoose: (Theme) -> Unit,
    onAboutClick: () -> Unit,
    onShowSavedClick: () -> Unit,
) {
    if (expanded) {
//    DropdownMenu(
//        expanded = expanded,
//        onDismissRequest = onDismiss,
//    ) {
        @Composable
        fun Item(
            icon: ImageVector,
            choice: Boolean? = null,
            @StringRes text: Int,
            onClick: () -> Unit,
        ) {
    Text(stringResource(text), modifier = Modifier.width(36.dp).clickable { onClick();onDismiss() })
//            DropdownMenuItem(
//                onClick = {
//                    onClick()
//                    onDismiss()
//                },
//                leadingIcon = { Icon(icon, contentDescription = null) },
//                trailingIcon = if (choice != null) {
//                    { RadioButton(choice, { onClick(); onDismiss() }) }
//                } else {
//                    null
//                },
//                text = { Text(stringResource(text)) }
//            )
        }

        Item(
            icon = Icons.Outlined.ListAlt,
            text = R.string.label_saved,
            onClick = { onShowSavedClick() },
        )
//        MenuDefaults.Divider(modifier = Modifier.padding(vertical = 4.dp))
        val theme = LocalTheme.current
        Item(
            icon = Icons.Outlined.WbSunny,
            text = R.string.label_theme_light,
            choice = theme == Theme.Light,
            onClick = { onThemeChoose(Theme.Light) },
        )
        Item(
            icon = Icons.Outlined.WbTwilight,
            text = R.string.label_theme_dark,
            choice = theme == Theme.Dark,
            onClick = { onThemeChoose(Theme.Dark) },
        )
        Item(
            icon = Icons.Outlined.Android,
            text = R.string.label_theme_system,
            choice = theme == Theme.System,
            onClick = { onThemeChoose(Theme.System) },
        )
////        MenuDefaults.Divider(modifier = Modifier.padding(vertical = 4.dp))
        Item(
            icon = Icons.Outlined.Info,
            text = R.string.label_about,
            onClick = { onAboutClick() },
        )
    }
}

@Composable
private fun ResetButton(onIntent: (Intent) -> Unit) {
    TextButton(onClick = { onIntent(EnterOptionsIntent.ResetAll) }) {
        Text(stringResource(R.string.action_reset))
    }
}

@Composable
private fun SaveButton(onIntent: (Intent) -> Unit) {
    TextButton(onClick = { onIntent(DilemmaIntent.Save) }) {
        Text(stringResource(R.string.action_save))
    }
}

@Composable
private fun SavedMessage() {
    TextButton(
        onClick = { },
        enabled = false,
        colors = ButtonDefaults.textButtonColors(
            disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.medium)
        ),
    ) {
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(stringResource(R.string.message_saved))
    }
}

@Composable
private fun OptionTextFields(
    dilemma: Dilemma,
    onIntent: (Intent) -> Unit,
    focusRequester: FocusRequester,
) {
    val addedFocusRequester = remember { FocusRequester() }
    dilemma.LaunchWhenOptionAdded {
        addedFocusRequester.requestFocus()
    }

    for (field in dilemma.render()) {
        if (field.focused) {
            LaunchedEffect(focusRequester) { focusRequester.requestFocus() }
        }
        OptionTextField(
            value = field.value,
            onValueChange = { value -> onIntent(EnterOptionsIntent.ChangeText(value, field.id)) },
            onRemoveOption = { onIntent(EnterOptionsIntent.Remove(field.id)) },
            imeAction = field.imeAction,
            modifier = Modifier.chooseRequester(
                field = field,
                first = focusRequester,
                added = addedFocusRequester
            ),
            index = field.humanIndex,
            canRemove = dilemma.canRemove,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

private fun Modifier.chooseRequester(
    field: Dilemma.TextField,
    first: FocusRequester,
    added: FocusRequester,
) = when {
    field.focused -> focusRequester(first)
    field.isLast -> focusRequester(added)
    else -> this
}

@Composable
private fun MakeChoiceButton(onActionClick: () -> Unit) {
    LargeFloatingActionButton(
        onClick = onActionClick,
        content = {
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = stringResource(R.string.action_make_choice)
            )
        },
    )
}

@Composable
private fun PasteButton(onIntent: (Intent) -> Unit) {
    val clipboardManager = LocalClipboardManager.current
    TextButton(onClick = { onIntent(EnterOptionsIntent.Add(clipboardManager.textOrEmpty)) }) {
        Icon(Icons.Default.ContentPaste, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Paste option")
    }
}

private val ClipboardManager.textOrEmpty get() = getText()?.toString() ?: ""

@Composable
private fun AddOptionButton(
    modifier: Modifier,
    onIntent: (Intent) -> Unit,
) {
    ElevatedButton(
        onClick = { onIntent(EnterOptionsIntent.AddNew) }, // TODO: Focus new field
        modifier = modifier,
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(stringResource(R.string.action_add_option))
    }
}
