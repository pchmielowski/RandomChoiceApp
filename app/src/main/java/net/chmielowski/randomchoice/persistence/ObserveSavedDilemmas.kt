package net.chmielowski.randomchoice.persistence

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.chmielowski.randomchoice.Database
import net.chmielowski.randomchoice.core.Dilemma
import net.chmielowski.randomchoice.core.DilemmaId

internal fun interface ObserveSavedDilemmas {

    operator fun invoke(): Flow<List<Pair<DilemmaId, Dilemma>>>
}

internal class ObserveSavedDilemmasImpl(private val database: Database) : ObserveSavedDilemmas {

    override operator fun invoke() = database.choiceQueries
        .selectAllDilemmas()
        .asFlow()
        .mapToList()
        .map { list ->
            list
                .groupBy(SelectAllDilemmas::id)
                .mapValues { (_, options) ->
                    options
                        .map(SelectAllDilemmas::name)
                        .let(::Dilemma)
                }
                .toList()
        }
}
