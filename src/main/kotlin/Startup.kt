import core.updateExtracts
import io.javalin.Javalin
import zip.Request
import zip.download
import java.time.LocalDateTime
import kotliquery.*

/**
 * Created by veenr on 23-6-2017.
 */

val version = "0.1"



val session = sessionOf("jdbc:h2:mem:hello", "user", "pass")

fun main(args: Array<String>) {
    val app = Javalin.create().port(6999).start()

    with(app) {
        get("/info") { ctx -> ctx.result("Feature-To-Extracts (K-style) ${version}") }

        get("/ping") { ctx -> ctx.result(LocalDateTime.now().toString()) }

        post("/pong") { ctx -> ctx.result(ctx.body()) }

        post("/process") { ctx ->
            val request = ctx.bodyAsClass(Request::class.java)
            println(request)
            val file = download(request.uri, request.zipped)
            updateExtracts(file!!, request)
        }
    }
}