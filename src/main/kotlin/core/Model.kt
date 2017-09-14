package core

import com.cognitect.transit.Keyword
import java.time.LocalDate
import java.util.*

data class Request(val dataset: String = "", val extractTypes: List<String> = emptyList(), val uri: String = "", val zipped: Boolean = true, val uniqueVersions: Boolean = true)

data class Changelog(val version: String, val collection: String, val lines: List<Feature>)

data class Feature(var action: String = "",
                    var featureType: String = "",
                   var version: UUID? = null,
                   var tiles: List<Int>? = null,
                   var xml: String = "",
                   var validFrom: Date? = null,
                   var validTo: Date? = null,
                   var publicationDate: LocalDate? = null)