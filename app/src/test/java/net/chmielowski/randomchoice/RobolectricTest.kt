package net.chmielowski.randomchoice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.chmielowski.randomchoice.ui.screen.input.DropdownMenuStrategy
import org.junit.After
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
internal class RobolectricTest : BehaviorTest() {

    override val menuStrategy: DropdownMenuStrategy = FakeDropdownMenuStrategy

    @After
    fun tearDown() = stopKoin()
}

@Suppress("TestFunctionName")
private object FakeDropdownMenuStrategy : DropdownMenuStrategy {

    @Composable
    override fun Container(content: @Composable () -> Unit) = content()

    @Composable
    override fun Menu(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        content: @Composable () -> Unit
    ) {
        if (expanded) {
            content()
        }
    }

    @Composable
    override fun Item(
        icon: ImageVector,
        choice: Boolean?,
        text: Int,
        onClick: () -> Unit,
        onDismiss: () -> Unit,
    ) = Text(
        stringResource(text),
        modifier = Modifier.size(10.dp).clickable { onClick(); onDismiss() },
        fontSize = 1.sp
    )

    @Composable
    override fun Divider(modifier: Modifier) {
    }
}
