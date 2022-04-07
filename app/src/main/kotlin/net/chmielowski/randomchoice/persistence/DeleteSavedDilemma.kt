package net.chmielowski.randomchoice.persistence

import net.chmielowski.randomchoice.Database
import net.chmielowski.randomchoice.core.DilemmaId

internal fun interface DeleteSavedDilemma {

    suspend operator fun invoke(id: DilemmaId)
}

internal class DeleteSavedDilemmaImpl(
    private val database: Database,
    private val task: NonCancellableTask,
) : DeleteSavedDilemma {

    override suspend fun invoke(id: DilemmaId) = task.run {
        database.choiceQueries.deleteDilemma(id)
    }
}
