package core

import com.cognitect.transit.Keyword
import com.cognitect.transit.impl.KeywordImpl
import com.github.andrewoma.kommon.collection.chunked
import zip.Changelog
import zip.Request
import zip.downloadAndReadFile
import zip.parse
import java.io.File

/**
 * Created by veenr on 8-9-2017.
 */


val KEYWORD_ACTION = KeywordImpl("_action")

val INSERT_ACTIONS = listOf("new", "change", "close")
val DELETE_ACTIONS = listOf("delete", "change", "close")

fun updateExtracts(file: File, request: Request) {
    val lines = downloadAndReadFile(request.uri, request.zipped)
    val changelog = parseChangeLog(lines)

    //  Validate the changelog returned, skipped for now

    processChanges(changelog, request)
}


fun parseChangeLog(logLines: List<String>) : Changelog {
    val changeLogVersion = logLines.first()
    val collection = parse(logLines.component2())
    println("Version: ${changeLogVersion} containing collection ${collection}")

    val features = logLines.drop(2).map { parse(it) }
    println("feature count: ${features.size}")
    val kw = KeywordImpl ("collection")

   return Changelog(version = changeLogVersion, collection = collection[kw]!!, lines = features)
}


fun processChanges(changelog: Changelog, request: Request) {
    val batchSize = 4000
    val log = changelog.lines.asSequence()
    for (batch in log.chunked(batchSize)) {
        transformAndAddExtract(request, changelog.collection, changelog.lines.filter (predicate = ::changeInsertRecords))
        deleteExtractsWithVersion(request, changelog.collection, changelog.lines.filter(predicate = ::deleteRecords))
    }
}



fun transformAndAddExtract(request: Request, collection: String, data: List<Map<Keyword, String>>) {
executeQuery("select id from dummy", listOf()) { r ->  }

}


fun deleteExtractsWithVersion(request: Request, collection: String, data: List<Map<Keyword, String>>) {

}


fun changeInsertRecords (value: Map<Keyword, String>): Boolean {
    return value[KEYWORD_ACTION] in INSERT_ACTIONS
}


fun deleteRecords (value: Map<Keyword, String>): Boolean {
    return value[KEYWORD_ACTION] in DELETE_ACTIONS
}

