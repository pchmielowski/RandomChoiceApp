package net.chmielowski.randomchoice

import androidx.test.ext.junit.runners.AndroidJUnit4
import net.chmielowski.randomchoice.core.Choice
import net.chmielowski.randomchoice.persistence.NonCancellableTask
import net.chmielowski.randomchoice.persistence.SaveDilemmaImpl
import net.chmielowski.randomchoice.ui.theme.Theme
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class ScreenshotTest1 : AbstractScreenshotTest(
    choice = FakeChoice("Pizza"),
    theme = Theme.Light,
) {

    @Test
    fun takeScreenshot() {
        enterOption1("Pizza")
        enterOption2("Salad")
        takeScreenshot("1")

        clickMakeChoice()
        takeScreenshot("2")
    }
}

@RunWith(AndroidJUnit4::class)
internal class ScreenshotTest2 : AbstractScreenshotTest(
    choice = Choice { error("Not used in this test!") },
    theme = Theme.Light,
    prepopulateDatabase = PrepopulateWithSavedChoices(),
) {

    @Test
    fun takeScreenshot() {
        navigateToSaved()

        takeScreenshot("3")
    }
}

@Suppress("SpellCheckingInspection")
@RunWith(AndroidJUnit4::class)
internal class ScreenshotTest3 : AbstractScreenshotTest(
    choice = FakeChoice("Intouchables"),
    theme = Theme.Dark,
) {

    @Test
    fun takeScreenshot() {
        clickAddOption()
        clickAddOption()
        enterOption1("Godfather")
        enterOption3("Once")
        enterOption2("Intouchables")
        enterOption4("Forrest Gump")
        takeScreenshot("4")
    }
}

private class FakeChoice(private val chosen: String) : Choice {

    override fun make(options: List<String>) = options.indexOfFirst { it == chosen }
}

private class PrepopulateWithSavedChoices : PrepopulateDatabase {

    @Suppress("SpellCheckingInspection")
    override fun invoke(database: Database) {
        val saveDilemma = SaveDilemmaImpl(database, NonCancellableTask.fake)
        saveDilemma(
            "Pizza",
            "Salad",
        )
        saveDilemma(
            "Espresso",
            "Cappuccino",
            "Latte macchiato",
        )
        saveDilemma(
            "Greece",
            "Italy",
            "Spain",
            "Portugal",
        )
        saveDilemma(
            "Take a nap",
            "Work hard",
        )
        saveDilemma(
            "Godfather",
            "Once",
            "Intouchables",
            "Forrest Gump",
        )
    }
}

internal abstract class AbstractScreenshotTest(
    override val choice: Choice,
    override val prepopulateDatabase: PrepopulateDatabase = PrepopulateDatabase {},
    theme: Theme,
) : AbstractTest() {

    override val preference = fakeThemePreference(theme)
}
