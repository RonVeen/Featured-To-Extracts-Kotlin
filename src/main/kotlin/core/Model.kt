package core

import com.cognitect.transit.Keyword
import java.time.LocalDate

data class Request(val dataset: String = "", val extractTypes: List<String> = emptyList(), val uri: String = "", val zipped: Boolean = true, val uniqueVersions: Boolean = true)

data class Changelog(val version: String, val collection: String, val lines: List<Map<Keyword, String>>)

data class Feature(val featureType: String,
                   val version: String,
                   var tiles: List<Integer>? = null,
                   val xml: String,
                   val validFrom: LocalDate,
                   val validTo: LocalDate,
                   var publicationDate: LocalDate? = null)