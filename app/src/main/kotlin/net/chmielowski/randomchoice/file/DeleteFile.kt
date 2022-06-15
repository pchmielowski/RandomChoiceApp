package net.chmielowski.randomchoice.file

import java.io.File

internal class DeleteFile {

    operator fun invoke(file: File) {
        file.delete()
    }
}