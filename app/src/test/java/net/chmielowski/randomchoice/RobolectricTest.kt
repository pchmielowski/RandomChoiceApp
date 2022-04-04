package net.chmielowski.randomchoice

import org.junit.After
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
internal class RobolectricTest : BehaviorTest() {

    override val isRobolectric get() = true

    @After
    fun tearDown() = stopKoin()
}
