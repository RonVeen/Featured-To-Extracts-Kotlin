package core

object Template {

    private var templates:MutableMap<String, String> = hashMapOf()

    fun clear() {
        templates.clear()
    }


    fun add(pair: Pair<String, String>) {
        add(pair.first, pair.second)
    }

    fun add(key: String, template: String) {
        templates.put(key = key, value = template)
    }

    fun find(key: String): String? {
        return templates[key]
    }

}