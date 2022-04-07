package net.chmielowski.randomchoice

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.check.checkKoinModules
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(
    instrumentedPackages = ["androidx.loader.content"],
    /* Can not be [net.chmielowski.randomchoice.RandomChoiceApplication] as it also starts Koin */
    application = Application::class,
)
class DiModuleTest {

    @Test
    fun checkDiModule() = checkKoinModules(
        listOf(
            androidContextModule(),
            diModule,
        ),
    )

    private fun androidContextModule() = module {
        factory { ApplicationProvider.getApplicationContext<Context>() }
    }
}
