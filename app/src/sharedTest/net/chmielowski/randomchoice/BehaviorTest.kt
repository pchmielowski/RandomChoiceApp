package net.chmielowski.randomchoice

import org.junit.Test

internal abstract class BehaviorTest : AbstractTest() {

    protected open val isRobolectric = false

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
        // Robolectric tests have problems with DropdownMenu: test hangs after clicking on menu button.
        if (isRobolectric) {
            return
        }

        enterOption1("Pizza")
        enterOption2("Salad")
        clickAddOption()
        enterOption3("Sandwich")

        clickSave()
        navigateToSaved()

        assertSavedOptionsArePresent("Pizza or Salad or Sandwich")
    }

    @Test
    fun removesSavedOptions() {
        // Robolectric tests have problems with DropdownMenu: test hangs after clicking on menu button.
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
    fun reusesSavedOptions() {
        // Robolectric tests have problems with DropdownMenu: test hangs after clicking on menu button.
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
