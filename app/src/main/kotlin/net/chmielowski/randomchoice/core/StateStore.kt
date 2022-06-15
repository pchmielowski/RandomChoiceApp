package net.chmielowski.randomchoice.core

import android.net.Uri
import android.os.Parcelable
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import net.chmielowski.randomchoice.core.Intent.DilemmaIntent
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.Add
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.AddNew
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.ChangeOption
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.ClickOption
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.OnCameraResult
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.Remove
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.ResetAll
import net.chmielowski.randomchoice.core.Intent.EnterOptionsIntent.SelectMode
import net.chmielowski.randomchoice.core.Intent.MakeChoice
import net.chmielowski.randomchoice.core.Intent.SetTheme
import net.chmielowski.randomchoice.file.CreateFile
import net.chmielowski.randomchoice.file.DeleteFile
import net.chmielowski.randomchoice.persistence.DeleteSavedDilemma
import net.chmielowski.randomchoice.persistence.SaveDilemma
import net.chmielowski.randomchoice.persistence.UndeleteSavedDilemma
import net.chmielowski.randomchoice.ui.theme.Theme
import net.chmielowski.randomchoice.ui.theme.ThemePreference
import java.io.File

internal fun createStateStore(
    factory: MainExecutor.Factory,
    state: State = State(),
) = DefaultStoreFactory().create(
    initialState = state,
    executorFactory = factory::create,
    reducer = { it },
)

internal sealed interface Intent {

    sealed interface EnterOptionsIntent : Intent {

        data class ChangeOption(val option: Option, val id: OptionId) : EnterOptionsIntent

        object AddNew : EnterOptionsIntent

        data class Add(val option: Option) : EnterOptionsIntent

        data class Remove(val id: OptionId) : EnterOptionsIntent

        object ResetAll : EnterOptionsIntent

        data class SelectMode(val mode: Mode) : EnterOptionsIntent

        data class OnCameraResult(val success: Boolean) : EnterOptionsIntent

        data class ClickOption(val option: OptionId) : EnterOptionsIntent
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
    val pendingPhotoRequest: PhotoRequest? = null,
) : Parcelable {

    val showResetButton get() = dilemma.canResetOrSave

    val showSaveButton
        get() = dilemma.canResetOrSave &&
                dilemma != lastSaved &&
                mode != Mode.Image

    val showSavedMessage get() = dilemma == lastSaved

    val mode get() = dilemma.mode

    @Parcelize
    data class PhotoRequest(
        val option: OptionId,
        val file: File,
    ) : Parcelable
}

internal sealed interface Label {

    object FocusFirstOptionInput : Label

    data class ShowResult(val result: Result) : Label

    object ShowDilemmaDeleted : Label

    data class TakePicture(val uri: Uri, val option: OptionId) : Label
}

internal class MainExecutor(
    private val choice: Choice,
    private val preference: ThemePreference,
    private val saveDilemma: SaveDilemma,
    private val deleteDilemma: DeleteSavedDilemma,
    private val undeleteDilemma: UndeleteSavedDilemma,
    private val createFile: CreateFile,
    private val deleteFile: DeleteFile,
) : CoroutineExecutor<Intent, Nothing, State, State, Label>() {

    @Suppress("ComplexMethod")
    override fun executeIntent(intent: Intent, getState: () -> State) {
        fun dispatchState(function: State.() -> State) {
            dispatch(getState().function())
        }
        when (intent) {
            is EnterOptionsIntent -> when (intent) {
                is ChangeOption -> dispatchState {
                    copy(dilemma = dilemma.update(intent.id, intent.option))
                }
                AddNew -> dispatchState { copy(dilemma = dilemma.addNew()) }
                is Add -> dispatchState { copy(dilemma = dilemma.add(intent.option)) }
                is Remove -> dispatchState { copy(dilemma = dilemma.remove(intent.id)) }
                ResetAll -> {
                    dispatchState { copy(dilemma = dilemma.reset()) }
                    if (getState().mode == Mode.Text) {
                        publish(Label.FocusFirstOptionInput)
                    }
                }
                is SelectMode -> dispatchState { copy(dilemma = dilemma.selectMode(intent.mode)) }
                is ClickOption -> {
                    val (file, uri) = createFile()
                    dispatchState {
                        copy(pendingPhotoRequest = State.PhotoRequest(intent.option, file))
                    }
                    publish(Label.TakePicture(uri, intent.option))
                }
                is OnCameraResult -> {
                    if (intent.success) {
                        dispatchState {
                            val request = pendingPhotoRequest!!
                            copy(
                                dilemma = dilemma.update(
                                    request.option,
                                    Option.Image(request.file)
                                ),
                                pendingPhotoRequest = null
                            )
                        }
                    } else {
                        deleteFile(getState().pendingPhotoRequest!!.file)
                        dispatchState {
                            copy(pendingPhotoRequest = null)
                        }
                        // TODO@ Show error
                    }
                }
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
                is DilemmaIntent.Delete -> scope.launch {
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
