package net.chmielowski.randomchoice.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import net.chmielowski.randomchoice.R

@Composable
internal fun createLaunchCamera(onResult: (Bitmap?) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(CameraResultContract(), onResult)
    val context = LocalContext.current
    return {
        @Suppress("SwallowedException")
        try {
            launcher.launch(Unit)
        } catch (e: ActivityNotFoundException) {
            context.showError()
        }
    }
}

private class CameraResultContract : ActivityResultContract<Unit, Bitmap?>() {

    override fun createIntent(context: Context, input: Unit) =
        Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    override fun parseResult(resultCode: Int, intent: Intent?) =
        if (resultCode == Activity.RESULT_OK) {
            intent?.extras?.getParcelable("data", Bitmap::class.java)
        } else {
            null
        }
}

private fun Context.showError() {
    Toast
        .makeText(this, R.string.error, Toast.LENGTH_LONG)
        .show()
}
