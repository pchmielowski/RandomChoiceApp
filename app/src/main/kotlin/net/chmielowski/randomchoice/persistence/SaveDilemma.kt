package net.chmielowski.randomchoice.persistence

import net.chmielowski.randomchoice.Database
import net.chmielowski.randomchoice.core.Dilemma
import net.chmielowski.randomchoice.core.DilemmaId
import net.chmielowski.randomchoice.core.Option

internal interface SaveDilemma {

    operator fun invoke(dilemma: Dilemma)

    operator fun invoke(vararg options: String)
}

internal class SaveDilemmaImpl(
    private val database: Database,
    private val task: NonCancellableTask,
) : SaveDilemma {

    override fun invoke(dilemma: Dilemma) = database.doSave(dilemma.persistable())

    override fun invoke(vararg options: String) = database.doSave(options.map(Option::Text))

    private fun Database.doSave(options: Iterable<Option>) = task.runNotObserving {
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
