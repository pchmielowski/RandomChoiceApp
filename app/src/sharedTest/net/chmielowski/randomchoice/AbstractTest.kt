@file:Suppress("SameParameterValue")

package net.chmielowski.randomchoice

import android.graphics.Bitmap
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.Dispatchers
import net.chmielowski.randomchoice.core.Choice
import net.chmielowski.randomchoice.core.MainExecutor
import net.chmielowski.randomchoice.core.createStateStore
import net.chmielowski.randomchoice.persistence.DeleteSavedDilemmaImpl
import net.chmielowski.randomchoice.persistence.NonCancellableTask
import net.chmielowski.randomchoice.persistence.ObserveSavedDilemmasImpl
import net.chmielowski.randomchoice.persistence.SaveDilemmaImpl
import net.chmielowski.randomchoice.ui.Content
import net.chmielowski.randomchoice.ui.screen.input.DropdownMenuStrategy
import net.chmielowski.randomchoice.ui.theme.Theme
import net.chmielowski.randomchoice.ui.theme.ThemePreference
import org.junit.Before
import org.junit.Rule
import java.io.FileOutputStream

internal abstract class AbstractTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    protected open val choice = Choice { 0 }

    protected open val theme = Theme.Light

    protected open val prepopulateDatabase = PrepopulateDatabase {}

    protected open val menuStrategy: DropdownMenuStrategy = DropdownMenuStrategy.Real()

    @Before
    fun setUp() {
        rule.setContent { Content() }
    }

    @Suppress("TestFunctionName")
    @Composable
    protected open fun Content() {
        val preference = fakeThemePreference(theme)
        val database = createInMemoryAndroidDatabase(rule, prepopulateDatabase)
        Content(
            preference = preference,
            observeSavedDilemmas = ObserveSavedDilemmasImpl(
                database,
                Dispatchers.Unconfined
            ),
            store = createStateStore({
                MainExecutor(
                    choice = choice,
                    preference = preference,
                    saveDilemma = SaveDilemmaImpl(database, NonCancellableTask.fake),
                    deleteDilemma = DeleteSavedDilemmaImpl(database, NonCancellableTask.fake),
                )
            }),
            menuStrategy = menuStrategy,
        )
    }

    protected fun assertOptionTextFieldsHaveValues(first: String, second: String) {
        rule
            .onNodeWithText(first)
            .assertExists()
        rule
            .onNodeWithText(second)
            .assertExists()
    }

    protected fun assertFirstAndSecondOptionsAreEmpty() {
        rule
            .onNodeWithText("Option 1")
            .assertExists()
        rule
            .onNodeWithText("Option 2")
            .assertExists()
    }

    protected fun enterOption1(text: String) {
        rule
            .onNodeWithText("Option 1")
            .performTextInput(text)
    }

    protected fun enterOption2(text: String) {
        rule
            .onNodeWithText("Option 2")
            .performTextInput(text)
    }

    protected fun enterOption3(text: String) {
        rule
            .onNodeWithText("Option 3")
            .performTextInput(text)
    }

    protected fun enterOption4(text: String) {
        rule
            .onNodeWithText("Option 4")
            .performTextInput(text)
    }

    protected fun clickMakeChoice() {
        rule
            .onNodeWithContentDescription(R.string.action_make_choice)
            .performClick()
    }

    protected fun clickAddOption() {
        rule
            .onNodeWithText(R.string.action_add_option)
            .performClick()
    }

    protected fun clickRemoveOption1() {
        rule
            .onNodeWithContentDescription("Remove option 1")
            .performClick()
    }

    protected fun clickReset() {
        rule
            .onNodeWithText(R.string.action_reset)
            .performClick()
    }

    protected fun clickReuse() {
        rule
            .onNodeWithText(R.string.action_reuse)
            .performClick()
    }

    protected fun assertThirdOptionDoesNotExist() {
        rule
            .onNodeWithText("Option 3")
            .assertDoesNotExist()
    }

    protected fun assertThirdOptionExists() {
        rule
            .onNodeWithText("Option 3")
            .assertExists()
    }

    protected fun clickBack() {
        rule
            .onNodeWithContentDescription(R.string.action_navigate_up)
            .performClick()
    }

    protected fun takeScreenshot(file: String) {
        rule.takeScreenshot(file)
    }

    protected fun assertResultHasText(text: String) {
        rule
            .onNodeWithContentDescription(R.string.label_chosen_option)
            .assertTextEquals(text)
    }

    protected fun assertResultDoesNotExist() {
        rule
            .onNodeWithContentDescription(R.string.label_chosen_option)
            .assertDoesNotExist()
    }

    protected fun navigateToSaved() {
        rule
            .onNodeWithContentDescription(R.string.label_more)
            .performClick()
        rule
            .onNodeWithText(R.string.label_saved)
            .performClick()
    }

    protected fun assertSaveIsDisplayed() {
        rule
            .onNodeWithText(R.string.action_save)
            .assertIsDisplayed()
    }

    protected fun clickSave() {
        rule
            .onNodeWithText(R.string.action_save)
            .performClick()
    }

    protected fun assertSavedMessageIsDisplayed() {
        rule
            .onNodeWithText(R.string.message_saved)
            .assertIsDisplayed()
    }

    protected fun assertSavedOptionsArePresent(options: String) {
        rule
            .onNodeWithText(options)
            .assertIsDisplayed()
    }

    protected fun assertEmptyViewIsPresent() {
        rule
            .onNodeWithText(R.string.message_empty)
            .assertIsDisplayed()
    }

    protected fun clickDelete() {
        rule
            .onNodeWithText(R.string.action_delete)
            .performClick()
    }
}

private fun ComposeContentTestRule.takeScreenshot(file: String) {
    onRoot()
        .captureToImage()
        .asAndroidBitmap()
        .save(file)
}

private fun Bitmap.save(file: String) {
    val path = InstrumentationRegistry.getInstrumentation().targetContext.filesDir.canonicalPath
    FileOutputStream("$path/$file.png").use { out ->
        compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    Log.d("Screenshot", "Saved screenshot to $path/$file.png")
}

private fun ComposeContentTestRule.onNodeWithText(@StringRes string: Int) =
    onNodeWithText(getString(string))

private fun ComposeContentTestRule.onNodeWithContentDescription(@StringRes string: Int) =
    onNodeWithContentDescription(getString(string))

private fun getString(string: Int) =
    InstrumentationRegistry.getInstrumentation().targetContext.getString(string)

internal fun fakeThemePreference(theme: Theme) = object : ThemePreference {

    override val current = theme

    override fun write(theme: Theme) {}
}
