package routes

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import services.UserService
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class RegisterRequest(val username: String, val email: String, val password: String)

@Serializable
data class RegisterResponse(
    @Contextual val userId: UUID,
    val message: String
)

fun Route.userRoutes() {
    route("/register") {
        post {
            val body = call.receive<RegisterRequest>()
            val hashedPassword = BCrypt.withDefaults().hashToString(12, body.password.toCharArray())

            val userId = try {
                UserService.insertUser(body.username, body.email, hashedPassword)
            } catch (e: Exception) {
                call.respondText("❌ Error: ${e.message}")
                return@post
            }

            call.respond(RegisterResponse(userId, "✅ Usuario registrado correctamente"))
        }
    }
}
