import com.example.configureHTTP
import com.example.configureMonitoring
import com.example.configureRouting
import com.example.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*
import services.UserService
import services.ScoreService


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    Databases.connectToPostgres(environment.config)
    routing {
        get("/") {
            call.respondText("🚀 Servidor conectado a PostgreSQL")
        }
    }
    environment.monitor.subscribe(ApplicationStarted) {
        val user1 = UserService.insertUser("ash", "ash@poke.com", "pikachu123")
        val user2 = UserService.insertUser("misty", "misty@poke.com", "togepi123")

        ScoreService.insertScore(user1, score = 3, correctAnswers = 3, time = 45)
        ScoreService.insertScore(user1, score = 2, correctAnswers = 2, time = 60)

        ScoreService.insertScore(user2, score = 4, correctAnswers = 4, time = 50)
        ScoreService.insertScore(user2, score = 5, correctAnswers = 5, time = 40)

        println("Datos insertados correctamente.")
    }
    configureSerialization()

    configureHTTP()
    configureMonitoring()
    configureRouting()
}
