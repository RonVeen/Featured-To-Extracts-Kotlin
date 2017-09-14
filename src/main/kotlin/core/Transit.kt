package core

import com.cognitect.transit.Keyword
import com.cognitect.transit.TaggedValue
import com.cognitect.transit.TransitFactory
import java.io.ByteArrayInputStream
import java.util.*

fun parse(text: String): Map<Keyword, Any?>  {
    try {
        val input = ByteArrayInputStream(text.toByteArray())
        val reader = TransitFactory.reader(TransitFactory.Format.JSON, input)
        val obj = reader.read<Map<Keyword, Any?>>()
        return obj
    } catch (e: Throwable) {
        throw RuntimeException(e)
    }

}



fun transform(input: Map<Keyword, Any?>): Feature {
    var feature = Feature()
    input.entries.forEach { e ->
        when(e.key.name) {
            "action" -> feature.action = e.value as String
            "version" -> feature.version =  e.value as UUID
            "valid-from" -> feature.validFrom = Date(taggedValueToLong(e.value))
            "tiles" -> feature.tiles = taggedValueToIntegerSet(e.value)
            "valid-to" -> feature.validTo = Date(taggedValueToLong(e.value))
        }
    }
    return feature
}


fun taggedValueToLong(value: Any?): Long {
    if (value != null) {
        val v = value as TaggedValue<Long>
        return v.rep
    }
    return 0L
}

fun taggedValueToIntegerSet(value: Any?): List<Int> {
    val hs: MutableSet<Int> = HashSet()
    if (value != null) {
        val v = value as HashSet<TaggedValue<String>>
        v?.forEach {
            val str = it?.rep
            hs.add(str.toInt())
        }
    }
    return hs.toList()
}
