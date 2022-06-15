package net.chmielowski.randomchoice.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import net.chmielowski.randomchoice.core.Intent

@Composable
internal fun rememberTakePictureLauncher(onIntent: (Intent) -> Unit) =
    rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        onIntent(Intent.EnterOptionsIntent.OnCameraResult(success = success))
    }
