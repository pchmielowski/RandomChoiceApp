package net.chmielowski.randomchoice

import org.junit.Test

internal abstract class BehaviorTest : AbstractTest() {

    open val isRobolectric = false

    @Test
    fun choosesOneOption() {
        if (isRobolectric) {
            return
        }
        enterOption1("Pizza")
        enterOption2("Salad")
        clickMakeChoice()

        assertResultHasText("Pizza")
    }

    @Test
    fun resetsOptions() {
        if (isRobolectric) {
            return
        }
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
        if (isRobolectric) {
            return
        }
        enterOption1("Pizza")
        enterOption2("Salad")
        clickMakeChoice()
        clickBack()

        assertResultDoesNotExist()
    }

    @Test
    fun keepsOptionsAfterMakingChoice() {
        if (isRobolectric) {
            return
        }
        enterOption1("Pizza")
        enterOption2("Salad")
        clickMakeChoice()
        clickBack()

        assertOptionTextFieldsHaveValues("Pizza", "Salad")
    }

    @Test
    fun savesOptions() {
        if (isRobolectric) {
            return
        }
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
        if (isRobolectric) {
            return
        }
        enterOption1("Pizza")
        enterOption2("Salad")
        clickSave()
        navigateToSaved()

        clickDelete()
        assertEmptyViewIsPresent()
    }

    @Test
    fun undoesRemovingSavedOptions() {
        if (isRobolectric) {
            return // Probably a bug in the TextField
        }
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
        if (isRobolectric) {
            return
        }
        enterOption1("Pizza")
        enterOption2("Salad")
        clickSave()
        clickReset()

        navigateToSaved()
        clickReuse()
        assertOptionTextFieldsHaveValues("Pizza", "Salad")
    }
}
