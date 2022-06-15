package net.chmielowski.randomchoice.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import net.chmielowski.randomchoice.utils.uri
import java.io.File

internal class CreateFile(private val context: Context) {

    operator fun invoke(): Pair<File, Uri> {
        val directory =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: error("TODO@")
        val file = File.createTempFile("Random Choice", ".jpg", directory)
        return file to file.uri(context)
    }
}
