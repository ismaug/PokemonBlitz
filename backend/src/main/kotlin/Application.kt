import com.example.configureHTTP
import com.example.configureMonitoring
import com.example.configureRouting
import com.example.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import services.ScoreService
import services.insertSampleData
import services.users


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    Databases.connectToPostgres(environment.config)

    // Solo inserta si no hay usuarios
    val hasUsers = transaction {
        !users.selectAll().empty()
    }

    if (!hasUsers) {
        insertSampleData()
        println("⚡ Datos de ejemplo insertados automáticamente.")
    } else {
        println("ℹ️ Usuarios ya existen. No se insertó nada.")
        val topScores = ScoreService.getTop5Scores()
        topScores.forEachIndexed { index, (username, score, time) ->
            println("🏅 #${index + 1}: $username - Score: $score - Time: $time sec")
        }
    }

    routing {
        get("/") {
            call.respondText("🚀 Servidor conectado a PostgreSQL")
        }
    }

    configureSerialization()
    configureHTTP()
    configureMonitoring()
    configureRouting()
}
