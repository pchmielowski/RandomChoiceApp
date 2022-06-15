@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.flow
import net.chmielowski.randomchoice.core.Dilemma
import net.chmielowski.randomchoice.core.DilemmaId
import net.chmielowski.randomchoice.core.Option
import net.chmielowski.randomchoice.core.Option.Image
import net.chmielowski.randomchoice.core.Result
import net.chmielowski.randomchoice.core.State
import net.chmielowski.randomchoice.ui.screen.about.AboutScreen
import net.chmielowski.randomchoice.ui.screen.about.LibrariesScreen
import net.chmielowski.randomchoice.ui.screen.about.LicenseScreen
import net.chmielowski.randomchoice.ui.screen.input.DropdownMenuStrategy
import net.chmielowski.randomchoice.ui.screen.input.InputScreen
import net.chmielowski.randomchoice.ui.screen.result.ResultScreen
import net.chmielowski.randomchoice.ui.screen.saved.SavedScreen
import net.chmielowski.randomchoice.utils.Loadable

@Preview
@Composable
internal fun InputScreenTextPreview() {
    InputScreen(
        navigator = EmptyDestinationsNavigator,
        state = State(Dilemma()),
        onIntent = {},
        focusRequester = FocusRequester(),
        menuStrategy = DropdownMenuStrategy.Real(),
    )
}

@Preview
@Composable
internal fun InputScreenImagePreview() {
    InputScreen(
        navigator = EmptyDestinationsNavigator,
        state = State(Dilemma(listOf(Image(), Image(), Image(), Image(), Image()))),
        onIntent = {},
        focusRequester = FocusRequester(),
        menuStrategy = DropdownMenuStrategy.Real(),
    )
}

@Preview
@Composable
internal fun ResultScreenPreview() {
    ResultScreen(
        navigator = EmptyDestinationsNavigator,
        result = Result(listOf(Option.Text("Pizza")), 0),
    )
}

@Preview
@Composable
internal fun SavedScreenPreview() {
    val dilemmas = listOf(
        DilemmaId(0) to Dilemma(listOf(Option.Text("Pizza"), Option.Text("Salad"))),
        DilemmaId(1) to Dilemma(listOf(Option.Text("Ski"), Option.Text("Bike"))),
    )
    SavedScreen(
        navigator = EmptyDestinationsNavigator,
        loadable = Loadable.Loaded(dilemmas),
        onIntent = {},
        labels = flow { },
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
