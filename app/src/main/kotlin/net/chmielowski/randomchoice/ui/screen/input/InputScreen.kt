@file:Suppress("FunctionName", "TooManyFunctions")

package net.chmielowski.randomchoice.ui.screen.input

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
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
import net.chmielowski.randomchoice.core.Dilemma.OptionField
import net.chmielowski.randomchoice.core.Intent
import net.chmielowski.randomchoice.core.Intent.DilemmaIntent
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent
import net.chmielowski.randomchoice.core.Label
import net.chmielowski.randomchoice.core.Label.FocusFirstOptionInput
import net.chmielowski.randomchoice.core.Label.ShowDilemmaDeleted
import net.chmielowski.randomchoice.core.Label.ShowResult
import net.chmielowski.randomchoice.core.Mode
import net.chmielowski.randomchoice.core.Option
import net.chmielowski.randomchoice.core.State
import net.chmielowski.randomchoice.ui.CircularRevealAnimation
import net.chmielowski.randomchoice.ui.screen.component.OptionTextField
import net.chmielowski.randomchoice.ui.screen.destinations.AboutScreenDestination
import net.chmielowski.randomchoice.ui.screen.destinations.ResultScreenDestination
import net.chmielowski.randomchoice.ui.screen.destinations.SavedScreenDestination
import net.chmielowski.randomchoice.ui.theme.LocalTheme
import net.chmielowski.randomchoice.ui.theme.Theme
import net.chmielowski.randomchoice.ui.widgets.Scaffold
import net.chmielowski.randomchoice.ui.widgets.rememberScrollBehavior
import net.chmielowski.randomchoice.utils.Observe
import net.chmielowski.randomchoice.utils.createLaunchCamera
import net.chmielowski.randomchoice.utils.stringResource

@OptIn(ExperimentalCoroutinesApi::class)
@Destination(start = true)
@Composable
internal fun InputScreen(
    navigator: DestinationsNavigator,
    store: Store<Intent, State, Label>,
    menuStrategy: DropdownMenuStrategy,
) {
    val state by store.states.collectAsState(State())
    val focusRequester = remember { FocusRequester() }
    store.labels.Observe { label ->
        when (label) {
            is ShowResult -> navigator.navigate(ResultScreenDestination(label.result))
            FocusFirstOptionInput -> focusRequester.requestFocus()
            ShowDilemmaDeleted -> {}
        }
    }
    InputScreen(
        navigator = navigator,
        state = state,
        onIntent = store::accept,
        focusRequester = focusRequester,
        menuStrategy = menuStrategy,
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
    menuStrategy: DropdownMenuStrategy,
) {
    var transitionVisible by remember { mutableStateOf(false) }
    val scrollBehavior = rememberScrollBehavior()
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
                menuStrategy = menuStrategy,
                mode = state.mode,
                onSelectMode = { onIntent(EnterOptionsIntent.SelectMode(it)) },
            )
        },
        scrollBehavior = scrollBehavior,
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
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .run {
                    when (state.dilemma.mode) {
                        Mode.Text -> verticalScroll(rememberScrollState())
                        Mode.Image -> this
                    }
                }
                .padding(16.dp),
        ) {
            if (state.mode == Mode.Image) {
                PhotoModeBanner()
            }
            OptionFields(
                dilemma = state.dilemma,
                onIntent = onIntent,
                focusRequester = focusRequester,
            )
            if (state.dilemma.mode == Mode.Text) {
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    PasteButton(onIntent = onIntent)
                    Spacer(modifier = Modifier.width(8.dp))
                    AddOptionButton(
                        onIntent = onIntent,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Spacer(modifier = Modifier.height(100.dp)) // Let the user scroll content up.
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoModeBanner() {
    ElevatedCard {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(Icons.Outlined.Info, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.message_photo_mode_experimental),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
    Spacer(modifier = Modifier.height(18.dp))
}

@Suppress("LongParameterList")
@Composable
internal fun MenuButton(
    onThemeChoose: (Theme) -> Unit,
    onAboutClick: () -> Unit,
    onShowSavedClick: () -> Unit,
    menuStrategy: DropdownMenuStrategy,
    mode: Mode,
    onSelectMode: (Mode) -> Unit,
) {
    menuStrategy.Container {
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
            mode = mode,
            onEnterModeClick = onSelectMode,
            strategy = menuStrategy,
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun DropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onThemeChoose: (Theme) -> Unit,
    onAboutClick: () -> Unit,
    onShowSavedClick: () -> Unit,
    mode: Mode,
    onEnterModeClick: (Mode) -> Unit,
    strategy: DropdownMenuStrategy,
) {
    strategy.Menu(expanded, onDismiss) {
        @Composable
        fun Item(
            icon: ImageVector,
            choice: Boolean? = null,
            @StringRes text: Int,
            onClick: () -> Unit,
        ) = strategy.Item(
            icon = icon,
            choice = choice,
            text = text,
            onClick = onClick,
            onDismiss = onDismiss,
        )

        when (mode) {
            Mode.Text -> Item(
                icon = Icons.Filled.CameraAlt,
                text = R.string.label_mode_photo,
                onClick = { onEnterModeClick(Mode.Image) },
            )
            Mode.Image -> Item(
                icon = Icons.Filled.ShortText,
                text = R.string.label_mode_text,
                onClick = { onEnterModeClick(Mode.Text) },
            )
        }
        Item(
            icon = Icons.Outlined.ListAlt,
            text = R.string.label_saved,
            onClick = { onShowSavedClick() },
        )
        strategy.Divider(modifier = Modifier.padding(vertical = 4.dp))
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
        strategy.Divider(modifier = Modifier.padding(vertical = 4.dp))
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
private fun OptionFields(
    dilemma: Dilemma,
    onIntent: (Intent) -> Unit,
    focusRequester: FocusRequester,
) {
    val addedFocusRequester = remember { FocusRequester() }
    dilemma.LaunchWhenOptionAdded {
        addedFocusRequester.requestFocus()
    }
    FieldsLayout(dilemma) { field ->
        Field(
            field = field,
            focusRequester = focusRequester,
            onIntent = onIntent,
            addedFocusRequester = addedFocusRequester,
            dilemma = dilemma,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FieldsLayout(
    dilemma: Dilemma,
    fieldContent: @Composable (OptionField) -> Unit,
) {
    val fields = dilemma.render()
    when (dilemma.mode) {
        Mode.Text -> {
            for (field in fields) {
                fieldContent(field)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Mode.Image -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(fields) { field ->
                    fieldContent(field)
                }
            }
        }
    }
}

@Composable
private fun Field(
    field: OptionField,
    focusRequester: FocusRequester,
    onIntent: (Intent) -> Unit,
    addedFocusRequester: FocusRequester,
    dilemma: Dilemma,
) {
    when (field) {
        is Dilemma.TextField -> TextField(
            field = field,
            focusRequester = focusRequester,
            onIntent = onIntent,
            addedFocusRequester = addedFocusRequester,
            dilemma = dilemma,
        )
        is Dilemma.ImageField -> ImageField(
            field = field,
            onOptionChange = { option ->
                onIntent(EnterOptionsIntent.ChangeOption(option, field.id))
            },
        )
    }
}

@Composable
private fun TextField(
    field: Dilemma.TextField,
    focusRequester: FocusRequester,
    onIntent: (Intent) -> Unit,
    addedFocusRequester: FocusRequester,
    dilemma: Dilemma,
) {
    if (field.focused) {
        LaunchedEffect(focusRequester) { focusRequester.requestFocus() }
    }
    OptionTextField(
        value = field.value,
        onValueChange = { value -> onIntent(EnterOptionsIntent.ChangeOption(value, field.id)) },
        onRemoveOption = { onIntent(EnterOptionsIntent.Remove(field.id)) },
        imeAction = field.imeAction,
        modifier = Modifier.chooseRequester(
            field = field,
            first = focusRequester,
            added = addedFocusRequester
        ),
        index = field.humanIndex,
        canRemove = dilemma.canRemove,
        label = field.label,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageField(
    field: Dilemma.ImageField,
    onOptionChange: (Option) -> Unit,
) {
    val launchCamera = createLaunchCamera(onResult = { bitmap ->
        onOptionChange(Option.Image(bitmap))
    })
    Card(
        modifier = Modifier
            .clickable(onClick = launchCamera)
    ) {
        val bitmap = field.value.bitmap
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(field.label),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
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
    TextButton(onClick = { onIntent(EnterOptionsIntent.Add(Option.Text(clipboardManager.textOrEmpty))) }) {
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
