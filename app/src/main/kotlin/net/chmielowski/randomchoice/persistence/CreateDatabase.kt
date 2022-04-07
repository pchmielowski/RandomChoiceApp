package net.chmielowski.randomchoice.persistence

import android.content.Context
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import net.chmielowski.randomchoice.Database
import net.chmielowski.randomchoice.core.DilemmaId

internal fun createPersistentAndroidDatabase(context: Context) =
    createAndroidDatabase(context, "main.db")

internal fun createAndroidDatabase(context: Context, name: String?) =
    createDatabase(AndroidSqliteDriver(Database.Schema, context, name))

internal fun createDatabase(driver: SqlDriver): Database {
    val dilemmaIdAdapter = object : ColumnAdapter<DilemmaId, Long> {
        override fun decode(databaseValue: Long) = DilemmaId(databaseValue)
        override fun encode(value: DilemmaId) = value.value
    }
    return Database(
        driver = driver,
        choiceAdapter = Choice.Adapter(dilemmaIdAdapter),
        optionAdapter = Option.Adapter(dilemmaIdAdapter),
    )
}
