package core

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.*


object Database {

    var connection: Connection? = null

    fun init(url: String, user: String, password: String) {
        connection = DriverManager.getConnection(url, user, password)
    }

    fun shutdown() {
        connection?.close()
    }
}



fun <T>executeQuery(query: String, params: List<Any>, resultSetProcessor: (ResultSet) -> T) : T {
    val statement = Database.connection?.prepareStatement(query)
    params.forEachIndexed { i, any -> statement?.setObject(i+1, any) }
    val resultSet = statement?.executeQuery()
    val outcome = resultSetProcessor(resultSet!!)
    resultSet.close()
    return outcome
}

fun executeQuery(query: String, params: List<Any>) : ResultSet? {
    val statement = Database.connection?.prepareStatement(query)
    params.forEachIndexed { i, any -> statement?.setObject(i+1, any) }
    return statement?.executeQuery()
}



fun ResultSet.forEachRow(action: (ResultSet) -> Unit) {
    while (this.next()) action(this)
    this.close()
}

fun <T>ResultSet.singleResult(action: (ResultSet) -> T) : T {
    this.next()
    val result = action(this)
    this.close()
    return result
}


fun <T>Statement.list(query: String, params: List<Any>, action: (ResultSet) -> T) : List<T> {
    val list: MutableList<T> = ArrayList<T>()
    val rs = core.executeQuery(query = query, params = params)
    while (rs!!.next()) {
        list.add(action(rs))
    }
    return list
}


fun ResultSet.count() : Int {
    return singleResult { rs -> rs.getInt(1) }
}



