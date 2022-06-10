package net.chmielowski.randomchoice.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
    val launcher = rememberLauncherForActivityResult(CameraResultContract(), onResult)
    val context = LocalContext.current
    return {
        @Suppress("SwallowedException") try {
            val file = createFile(context)
            launcher.launch(file)
        } catch (e: ActivityNotFoundException) {
            context.showError()
        }
    }
}

private class CameraResultContract : ActivityResultContract<Uri, Bitmap?>() {

    override fun createIntent(context: Context, input: Uri): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, input)
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

private fun createFile(context: Context): Uri? {
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
    val file = File.createTempFile("Random Choice", ".jpg", directory)
    return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
}
