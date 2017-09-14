package core

import com.cognitect.transit.Keyword
import com.cognitect.transit.TaggedValue
import com.cognitect.transit.impl.KeywordImpl
import com.cognitect.transit.impl.TaggedValueImpl
import com.github.andrewoma.kommon.collection.chunked
import zip.*
import core.*
import java.io.File
import java.time.LocalDate
import java.util.*

/**
 * Created by veenr on 8-9-2017.
 */


val KEYWORD_ACTION = "action"
val KEYWORD_VERSION = "version"
val KEYWORD_VALID_FROM = "valid-from"
val KEYWORD_VALID_TO = "valid-to"
val KEYWORD_TILES = "tiles"


val INSERT_ACTIONS = listOf("new", "change", "close")
val DELETE_ACTIONS = listOf("delete", "change", "close")

fun updateExtracts(file: File, request: Request) {
    val lines = downloadAndReadFile(request.uri, request.zipped)
    val changelog = parseChangeLog(lines)

    //  Validate the changelog returned, skipped for now

    request.extractTypes.forEach { processChanges(changelog, request, it) }
//    processChanges(changelog, request, request.extractTypes)

}


fun parseChangeLog(logLines: List<String>) : Changelog {
    val changeLogVersion = logLines.first()
    val collection = parse(logLines.component2())
    println("Version: ${changeLogVersion} containing collection ${collection}")

    val features = logLines.drop(2).map { parseFeature(it) }
    println("feature count: ${features.size}")
    val kw = KeywordImpl ("collection")

   return Changelog(version = changeLogVersion, collection = "" , lines = features)
}


fun processChanges(changelog: Changelog, request: Request, extractType: String) {
    val batchSize = 4000
    val log = changelog.lines.asSequence()
    for (batch in log.chunked(batchSize)) {
        transformAndAddExtract(request, changelog.collection, extractType, batch.filter { it.action in INSERT_ACTIONS} )
        deleteExtractsWithVersion(request, changelog.collection, extractType, batch.filter { it.action in DELETE_ACTIONS })
    }
}



fun transformAndAddExtract(request: Request, collection: String, extractType: String, data: List<Feature>) {
    val features:MutableList<Feature> = ArrayList()
    data.forEach {
        val xml = applyTemplate(request = request, collection = collection, extractType = extractType)

    }
}


fun applyTemplate(request: Request, collection: String, extractType: String): String {
    val template = Template.find(templateKey(dataset = request.dataset, extractType = extractType, name = collection))
    return template ?: "<Dummy value>"
}


fun templateKey(dataset: String, extractType: String, name: String) = "${dataset}-${extractType}-${name}"


fun deleteExtractsWithVersion(request: Request, collection: String, extractType: String, data: List<Feature>) {

}


