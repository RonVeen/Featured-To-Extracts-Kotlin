package zip

import com.cognitect.transit.Keyword

data class Request(val dataset: String = "", val extractTypes: String = "", val uri: String = "", val zipped: Boolean = true, val uniqueVersions: Boolean = true)

data class Changelog(val version: String, val collection: String, val lines: List<Map<Keyword, String>>)