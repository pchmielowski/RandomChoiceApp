package net.chmielowski.randomchoice.persistence

import net.chmielowski.randomchoice.Database
import net.chmielowski.randomchoice.core.Dilemma
import net.chmielowski.randomchoice.core.DilemmaId

internal interface SaveDilemma {

    operator fun invoke(dilemma: Dilemma)

    operator fun invoke(vararg options: String)
}

internal class SaveDilemmaImpl(
    private val database: Database,
    private val task: NonCancellableTask,
) : SaveDilemma {

    override fun invoke(dilemma: Dilemma) = database.doSave(dilemma.persistable())

    override fun invoke(vararg options: String) = database.doSave(options.toList())

    private fun Database.doSave(options: Iterable<String>) = task.runNotObserving {
        transaction {
            choiceQueries.insertDilemma()
            val choice = choiceQueries
                .selectLastInsertedId()
                .executeAsOne()
                .let(::DilemmaId)
            for (option in options) {
                optionQueries.insertOption(option, choice)
            }
        }
    }
}
