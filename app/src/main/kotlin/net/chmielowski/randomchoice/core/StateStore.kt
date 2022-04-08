package net.chmielowski.randomchoice.core

import android.os.Parcelable
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.parcelize.Parcelize
import net.chmielowski.randomchoice.core.Intent.DilemmaIntent
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.Add
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.AddNew
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.ChangeText
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.Remove
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.ResetAll
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.SelectMode
import net.chmielowski.randomchoice.core.Intent.MakeChoice
import net.chmielowski.randomchoice.core.Intent.SetTheme
import net.chmielowski.randomchoice.persistence.DeleteSavedDilemma
import net.chmielowski.randomchoice.persistence.SaveDilemma
import net.chmielowski.randomchoice.persistence.UndeleteSavedDilemma
import net.chmielowski.randomchoice.ui.theme.Theme
import net.chmielowski.randomchoice.ui.theme.ThemePreference

internal fun createStateStore(
    factory: MainExecutor.Factory,
    state: State = State(),
) = DefaultStoreFactory.create(
    initialState = state,
    executorFactory = factory::create,
    reducer = { it },
)

internal sealed interface Intent {

    sealed interface EnterOptionsIntent : Intent {

        // TODO: ChangeText -> ChangeOption, text -> option
        data class ChangeText(val text: Option, val id: Int) : EnterOptionsIntent

        object AddNew : EnterOptionsIntent

        // TODO: text -> option
        data class Add(val text: Option) : EnterOptionsIntent

        data class Remove(val id: Int) : EnterOptionsIntent

        object ResetAll : EnterOptionsIntent

        data class SelectMode(val mode: Mode) : EnterOptionsIntent
    }

    data class SetTheme(val theme: Theme) : Intent

    object MakeChoice : Intent

    sealed interface DilemmaIntent : Intent {

        data class Reuse(val dilemma: Dilemma) : DilemmaIntent

        object Save : DilemmaIntent

        data class Delete(val dilemma: DilemmaId) : DilemmaIntent

        object UndoDeleting : DilemmaIntent
    }
}

@Parcelize
internal data class State(
    val dilemma: Dilemma = Dilemma(),
    private val lastSaved: Dilemma? = null,
    val lastDeleted: DilemmaId? = null,
    val mode: Mode = Mode.Text,
) : Parcelable {

    val showResetButton get() = dilemma.canResetOrSave

    val showSaveButton get() = dilemma.canResetOrSave && dilemma != lastSaved

    val showSavedMessage get() = dilemma == lastSaved
}

internal sealed interface Label {

    object FocusFirstOptionInput : Label

    data class ShowResult(val result: Result) : Label

    object ShowDilemmaDeleted : Label
}

internal class MainExecutor(
    private val choice: Choice,
    private val preference: ThemePreference,
    private val saveDilemma: SaveDilemma,
    private val deleteDilemma: DeleteSavedDilemma,
    private val undeleteDilemma: UndeleteSavedDilemma,
) : SuspendExecutor<Intent, Nothing, State, State, Label>() {

    @Suppress("ComplexMethod")
    override suspend fun executeIntent(intent: Intent, getState: () -> State) {
        fun dispatchState(function: State.() -> State) {
            dispatch(getState().function())
        }
        return when (intent) {
            is EnterOptionsIntent -> when (intent) {
                is ChangeText -> dispatchState {
                    copy(dilemma = dilemma.updateText(intent.id, intent.text))
                }
                AddNew -> dispatchState { copy(dilemma = dilemma.addNew()) }
                is Add -> dispatchState { copy(dilemma = dilemma.addShared(intent.text)) }
                is Remove -> dispatchState { copy(dilemma = dilemma.remove(intent.id)) }
                ResetAll -> {
                    dispatchState { copy(dilemma = dilemma.reset()) }
                    publish(Label.FocusFirstOptionInput)
                }
                is SelectMode -> dispatchState { copy(mode = intent.mode) }
            }
            MakeChoice -> {
                val result = getState().dilemma.choose(choice)
                publish(Label.ShowResult(result))
            }
            is SetTheme -> preference.write(intent.theme)
            is DilemmaIntent -> when (intent) {
                DilemmaIntent.Save -> {
                    val current = getState().dilemma
                    saveDilemma(current)
                    dispatchState { copy(lastSaved = current) }
                }
                is DilemmaIntent.Reuse -> dispatchState { copy(dilemma = intent.dilemma) }
                is DilemmaIntent.Delete -> {
                    deleteDilemma(intent.dilemma)
                    dispatchState { copy(lastDeleted = intent.dilemma) }
                    publish(Label.ShowDilemmaDeleted)
                }
                DilemmaIntent.UndoDeleting -> undeleteDilemma(getState().lastDeleted!!)
            }
        }
    }

    fun interface Factory {

        fun create(): MainExecutor
    }
}
