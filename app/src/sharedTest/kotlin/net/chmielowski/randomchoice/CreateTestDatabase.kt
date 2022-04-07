package net.chmielowski.randomchoice

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import net.chmielowski.randomchoice.persistence.createAndroidDatabase
import net.chmielowski.randomchoice.persistence.createDatabase

internal fun createJvmDatabase(): Database {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    Database.Schema.create(driver)
    return createDatabase(driver)
}

internal fun createInMemoryAndroidDatabase(
    rule: AndroidComposeTestRule<*, *>,
    prepopulateDatabase: PrepopulateDatabase = PrepopulateDatabase { },
): Database {
    val database = createAndroidDatabase(rule.activity, null)
    prepopulateDatabase(database)
    return database
}

internal fun interface PrepopulateDatabase {

    operator fun invoke(database: Database)
}
