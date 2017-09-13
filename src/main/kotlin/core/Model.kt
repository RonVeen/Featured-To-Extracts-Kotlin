package core

import kotliquery.Session
import kotliquery.sessionOf

object DB {

    var session: Session? = null

    fun init(url: String, user: String, password: String) {
        session = sessionOf(url=url, user=user, password = password, returnGeneratedKey = false)
    }
}