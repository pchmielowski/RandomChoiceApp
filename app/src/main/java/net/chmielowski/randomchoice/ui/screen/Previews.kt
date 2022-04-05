@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.flowOf
import net.chmielowski.randomchoice.core.Dilemma
import net.chmielowski.randomchoice.core.Result
import net.chmielowski.randomchoice.core.State
import net.chmielowski.randomchoice.ui.ResultScreen
import net.chmielowski.randomchoice.ui.screen.about.AboutScreen
import net.chmielowski.randomchoice.ui.screen.about.LibrariesScreen
import net.chmielowski.randomchoice.ui.screen.about.LicenseScreen
import net.chmielowski.randomchoice.ui.screen.input.InputScreen
import net.chmielowski.randomchoice.ui.screen.saved.SavedScreen

@Preview
@Composable
internal fun InputScreenPreview() {
    InputScreen(
        navigator = EmptyDestinationsNavigator,
        state = State(Dilemma()),
        onIntent = {},
        focusRequester = FocusRequester(),
    )
}

@Preview
@Composable
internal fun ResultScreenPreview() {
    ResultScreen(
        navigator = EmptyDestinationsNavigator,
        result = Result(listOf("Pizza"), 0),
    )
}

@Preview
@Composable
internal fun SavedScreenPreview() {
    SavedScreen(
        navigator = EmptyDestinationsNavigator,
        observeSavedDilemmas = { flowOf(emptyList()) },
        onIntent = {},
    )
}

@Preview
@Composable
internal fun AboutScreenPreview() {
    AboutScreen()
}

@Preview
@Composable
internal fun LibrariesScreenPreview() {
    LibrariesScreen()
}

@Preview
@Composable
internal fun LicenseScreenPreview() {
    LicenseScreen(
        navigator = EmptyDestinationsNavigator,
        libraryName = "Accompanist",
    )
}
