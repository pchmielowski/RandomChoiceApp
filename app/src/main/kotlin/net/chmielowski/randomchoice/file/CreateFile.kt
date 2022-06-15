package net.chmielowski.randomchoice.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import net.chmielowski.randomchoice.BuildConfig
import java.io.File

internal class CreateFile(private val context: Context) {

    operator fun invoke(): Pair<File, Uri> {
        val file = createTempFile()
        val uri = getUri(file)
        return file to uri
    }

    private fun createTempFile(): File {
        val directory = context
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: error("TODO@")
        return File.createTempFile("Random Choice", ".jpg", directory)
    }

    private fun getUri(file: File) =
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)

}
