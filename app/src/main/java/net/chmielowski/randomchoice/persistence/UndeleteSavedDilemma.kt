package net.chmielowski.randomchoice.persistence

import net.chmielowski.randomchoice.Database
import net.chmielowski.randomchoice.core.DilemmaId

internal fun interface UndeleteSavedDilemma {

    operator fun invoke(id: DilemmaId)
}

internal class UndeleteSavedDilemmaImpl(
    private val database: Database,
    private val task: NonCancellableTask,
) : UndeleteSavedDilemma {

    override fun invoke(id: DilemmaId) = task.runNotObserving {
        database.choiceQueries.undeleteDilemma(id)
    }
}
