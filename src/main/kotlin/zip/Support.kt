package zip

import com.cognitect.transit.Keyword
import com.cognitect.transit.Reader
import com.cognitect.transit.TaggedValue
import core.updateExtracts
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import com.cognitect.transit.TransitFactory
import core.Feature
import core.parse
import core.transform
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.util.*


fun firstFileFromZip(zippedFile: File): File?  {
    val zip = ZipFile(zippedFile)
    val file = ZipInputStream(FileInputStream(zippedFile))
    file.nextEntry.let {
        val tempFile = createTempFile("features", ".json")
        file.copyTo(FileOutputStream(tempFile))
        return tempFile
    }
    return null
}

fun download(uri: String, zipped: Boolean) : File? {
    val file = File.createTempFile("featured-to-extracts", if (zipped) ".zip" else "*.csv")
    var input: FileInputStream = FileInputStream(uri)
    input.copyTo(FileOutputStream(file))
    return file
}


fun downloadAndReadFile(uri: String, zipped: Boolean): List<String> {
    try {
        var file: File? = null
        val downloadedFile = download(uri, zipped)
        if (zipped) {
             file = firstFileFromZip(downloadedFile!!)
        } else {
            file = downloadedFile!!
        }
        return file!!.readLines()
    } catch (e: Exception) {
        return listOf()
    }
}

fun parseFeature(text: String): Feature {
    return transform(parse(text = text))
}



