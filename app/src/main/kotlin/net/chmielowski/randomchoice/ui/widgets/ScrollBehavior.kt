package net.chmielowski.randomchoice.ui.widgets

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun rememberScrollBehavior(): TopAppBarScrollBehavior {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    return remember { TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec) }
}
