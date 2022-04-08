package net.chmielowski.randomchoice

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Test

internal abstract class BehaviorTest : AbstractTest() {

    @Test
    fun choosesOneOption() {
        enterOption1("Pizza")
        enterOption2("Salad")
        clickMakeChoice()

        assertResultHasText("Pizza")
    }

    @Test
    fun resetsOptions() {
        enterOption1("Pizza")
        enterOption2("Salad")
        clickAddOption()

        clickReset()

        assertFirstAndSecondOptionsAreEmpty()
        assertThirdOptionDoesNotExist()
    }

    @Test
    fun addsNewOption() {
        clickAddOption()

        assertThirdOptionExists()
    }

    @Test
    fun removesNewOption() {
        clickAddOption()
        clickRemoveOption1()

        assertThirdOptionDoesNotExist()
    }

    @Test
    fun closesResultScreen() {
        enterOption1("Pizza")
        enterOption2("Salad")
        clickMakeChoice()
        clickBack()

        assertResultDoesNotExist()
    }

    @Test
    fun keepsOptionsAfterMakingChoice() {
        enterOption1("Pizza")
        enterOption2("Salad")
        clickMakeChoice()
        clickBack()

        assertOptionTextFieldsHaveValues("Pizza", "Salad")
    }

    @Test
    fun savesOptions() {
        enterOption1("Pizza")
        enterOption2("Salad")
        clickAddOption()
        enterOption3("Sandwich")
        clickAddOption()
        enterOption4("Sandwich") // To verify that duplicates are ignored.

        assertSaveIsDisplayed()
        clickSave()
        assertSavedMessageIsDisplayed()

        // Verify "Save" is displayed again after modification
        clickRemoveOption1()
        assertSaveIsDisplayed()

        navigateToSaved()
        assertSavedOptionsArePresent("Pizza or Salad or Sandwich")
    }

    @Test
    fun removesSavedOptions() {
        enterOption1("Pizza")
        enterOption2("Salad")
        clickSave()
        navigateToSaved()

        clickDelete()
        assertEmptyViewIsPresent()
    }

    @Test
    fun undoesRemovingSavedOptions() {
        enterOption1("Pizza")
        enterOption2("Salad")
        clickSave()
        navigateToSaved()

        clickDelete()
        clickUndo()
        assertSavedOptionsArePresent("Pizza or Salad")
    }

    @Test
    fun reusesSavedOptions() {
        enterOption1("Pizza")
        enterOption2("Salad")
        clickSave()
        clickReset()

        navigateToSaved()
        clickReuse()
        assertOptionTextFieldsHaveValues("Pizza", "Salad")
    }

    @Test
    fun switchesToPhotoMode() {
        rule
            .onNodeWithContentDescription(R.string.label_more)
            .performClick()
        rule
            .onNodeWithText(R.string.label_mode_photo)
            .performClick()
    }
}
