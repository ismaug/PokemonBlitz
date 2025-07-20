package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import services.ScoreService
import services.scores
import java.util.UUID

fun Route.scoreRoutes() {

    // ✅ Ruta protegida: Obtener los scores del usuario autenticado
    authenticate {
        get("/scores/user") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asString()

            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, "❌ Token inválido")
                return@get
            }

            val userScores = ScoreService.getScoresByUser(UUID.fromString(userId)).map {
                mapOf(
                    "score" to it[scores.score],
                    "correct_answers" to it[scores.correctAnswers],
                    "time" to it[scores.time]
                )
            }

            call.respond(userScores)
        }
    }

    // ✅ Ruta protegida: Obtener el top 5 global con username y fecha
    authenticate {
        get("/scores/top5") {
            val topScores = ScoreService.getTop5Scores()
            call.respond(topScores)
        }
    }
}
