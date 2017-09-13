package core

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import java.sql.ResultSet
import java.sql.Statement

/**
 * Created by veenr on 13-9-2017.
 */
class DatabaseTest {


    @Test
    fun setup() {
        Database.init(url = "jdbc:postgresql://localhost:5432/pdok", user = "postgres", password = "postgres")
    }

    @Test
    fun executeQuery() {
        setup()
        val sql = "select count(*) from datamanagement.copyright"
        var result = 0
        core.executeQuery(query = sql,
                          params = emptyList(),
                          resultSetProcessor = { rs -> rs.next()
                              result = rs.getInt(1)
                          } )
        assertNotEquals(result, 0)
        assertEquals(result, 7)
    }


    @Test
    fun selectAllValuesFromTable() {
        setup()
        val sql = "select * from datamanagement.copyright"
        val result: List<String> = core.executeQuery(query = sql,
                params = emptyList(),
                resultSetProcessor = { rs ->
                    val names:MutableList<String> = arrayListOf()
                    while(rs.next()) {
                        names.add(rs.getString(2))
                    }
                    names
                })
        assertNotEquals(result.size, 0)
        assertEquals(result.get(1), "PDM")
    }

    @Test
    fun selectAllValuesFromTableUsingList() {
        setup()
        val sql = "select * from datamanagement.copyright"
        val result: List<String>? = Database.connection?.createStatement()?.list(query = sql, params = emptyList()) { rs -> rs.getString(2)}
        assertNotEquals(result?.size, 0)
        assertEquals(result?.get(1), "PDM")
    }


    @Test
    fun selectValuesWithWhereCondition() {
        setup()
        val sql = "select * from datamanagement.copyright where name=?"
        val result: String? = core.executeQuery(query = sql,
                params = listOf("PDM"),
                resultSetProcessor = { rs ->
                    if (rs.next()) rs.getString(3) else null
                })
        assertNotNull(result)
        assertEquals(result, "http://creativecommons.org/publicdomain/mark/1.0/deed.nl")
    }


    @Test
    fun selectValuesUsingForEachRow() {
        setup()
        val sql = "select * from datamanagement.copyright"
        val results: MutableList<String> = arrayListOf()
        core.executeQuery(query = sql, params = emptyList())?.forEachRow {  r ->  results.add(r.getString(2))}
        assertNotEquals(results.size, 0)
        assertEquals(results.get(1), "PDM")
    }

    @Test
    fun selectSingleValueUsingSingelResult() {
        setup()
        val sql = "select count(*) from datamanagement.copyright"
        val count = core.executeQuery(query = sql, params = emptyList())?.singleResult {  r ->  r.getInt(1)}
        assertNotEquals(count, 0)
        assertEquals(count, 7)
    }

    @Test
    fun selectRecordCountUsingCount() {
        setup()
        val sql = "select count(*) from datamanagement.copyright"
        val count = core.executeQuery(query = sql, params = emptyList())?.count()
        assertNotEquals(count, 0)
        assertEquals(count, 7)
    }



    fun shutdown() {
    }

}