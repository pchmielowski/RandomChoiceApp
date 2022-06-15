package net.chmielowski.randomchoice.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import net.chmielowski.randomchoice.BuildConfig
import net.chmielowski.randomchoice.R
import java.io.File

@Composable
internal fun createLaunchCamera(onResult: (Bitmap?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(CameraResultContract(), onResult)
    return {
        try {
            launcher.launch(Unit)
        } catch (_: ActivityNotFoundException) {
            context.showError()
        }
    }
}

private class CameraResultContract : ActivityResultContract<Unit, Bitmap?>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, createFile(context).uri(context))
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?) =
        if (resultCode == Activity.RESULT_OK) {
            intent?.extras?.getParcelableCompat<Bitmap>("data")
        } else {
            null
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
