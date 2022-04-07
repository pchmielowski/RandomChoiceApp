package net.chmielowski.randomchoice.persistence

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.chmielowski.randomchoice.Database
import net.chmielowski.randomchoice.core.Dilemma
import net.chmielowski.randomchoice.core.DilemmaId
import kotlin.coroutines.CoroutineContext

internal fun interface ObserveSavedDilemmas {

    operator fun invoke(): Flow<List<Pair<DilemmaId, Dilemma>>>
}

internal class ObserveSavedDilemmasImpl(
    private val database: Database,
    private val context: CoroutineContext = Dispatchers.IO,
) : ObserveSavedDilemmas {

    override operator fun invoke() = database.choiceQueries
        .selectAllDilemmas()
        .asFlow()
        .mapToList(context)
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
