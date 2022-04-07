package net.chmielowski.randomchoice.persistence

import android.content.Context
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import net.chmielowski.randomchoice.Database
import net.chmielowski.randomchoice.core.DilemmaId
import net.chmielowski.randomchoice.core.Option
import net.chmielowski.randomchoice.persistence.Option as DbOption

internal fun createPersistentAndroidDatabase(context: Context) =
    createAndroidDatabase(context, "main.db")

internal fun createAndroidDatabase(context: Context, name: String?) =
    createDatabase(AndroidSqliteDriver(Database.Schema, context, name))

internal fun createDatabase(driver: SqlDriver): Database {
    val dilemmaIdAdapter = object : ColumnAdapter<DilemmaId, Long> {
        override fun decode(databaseValue: Long) = DilemmaId(databaseValue)
        override fun encode(value: DilemmaId) = value.value
    }
    val optionAdapter = object : ColumnAdapter<Option, String> {
        override fun decode(databaseValue: String) = Option(databaseValue)
        override fun encode(value: Option) = value.text
    }
    return Database(
        driver = driver,
        choiceAdapter = Choice.Adapter(dilemmaIdAdapter),
        optionAdapter = DbOption.Adapter(optionAdapter, dilemmaIdAdapter),
    )
}
