package net.chmielowski.randomchoice.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import net.chmielowski.randomchoice.BuildConfig
import net.chmielowski.randomchoice.R
import java.io.File

@Composable
internal fun createLaunchCamera(onResult: (Boolean) -> Unit): (File) -> Unit {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture(), onResult)
    return { file: File ->
        try {
            launcher.launch(file.uri(context))
        } catch (_: ActivityNotFoundException) {
            context.showError()
        }
    }
}

private fun Context.showError() {
    Toast
        .makeText(this, R.string.error, Toast.LENGTH_LONG)
        .show()
}

internal fun createFile(context: Context): File {
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: error("TODO@")
    return File.createTempFile("Random Choice", ".jpg", directory)
}

private fun File.uri(context: Context) =
    FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", this)
