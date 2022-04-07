package net.chmielowski.randomchoice.ui.screen.about

internal val libraries = listOf(
    Library(
        name = "Accompanist",
        url = "https://github.com/google/accompanist",
        license = accompanistLicense(),
    ),
    Library(
        name = "Compose destinations",
        url = "https://github.com/raamcosta/compose-destinations",
        license = destinationsLicense(),
    ),
    Library(
        name = "MVI Kotlin",
        url = "https://github.com/arkivanov/MVIKotlin",
        license = mviKotlinLicense(),
    ),
    Library(
        name = "SQLDelight",
        url = "https://github.com/cashapp/sqldelight/",
        license = sqlDelightLicense(),
    ),
    Library(
        name = "Koin",
        url = "https://github.com/InsertKoinIO/koin",
        license = koinLicense(),
    ),
    Library(
        name = "Junit 4",
        url = "https://github.com/junit-team/junit4",
        license = junitLicense(),
    ),
    Library(
        name = "AssertJ",
        url = "https://github.com/assertj/",
        license = assertJLicense(),
    ),
    Library(
        name = "Robolectric",
        url = "https://github.com/robolectric/robolectric",
        license = robolectricLicense(),
    ),
    Library(
        name = "LeakCanary",
        url = "https://github.com/square/leakcanary",
        license = leakCanaryLicense(),
    ),
)
