package routes

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import services.UserService
import services.users
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID
import JwtConfig
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll

@Serializable
data class RegisterRequest(val username: String, val email: String, val password: String)

@Serializable
data class RegisterResponse(
    @Contextual val userId: UUID,
    val message: String
)

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class LoginResponse(val token: String, val expiresInMs: Long)

fun Route.userRoutes() {

    // ✅ Registro de usuario
    route("/register") {
        post {
            val body = call.receive<RegisterRequest>()
            val hashedPassword = BCrypt.withDefaults().hashToString(12, body.password.toCharArray())

            val userId = try {
                UserService.insertUser(body.username, body.email.trim().lowercase(), hashedPassword)
            } catch (e: Exception) {
                call.respondText("❌ Error: ${e.message}")
                return@post
            }

            call.respond(RegisterResponse(userId, "✅ Usuario registrado correctamente"))
        }
    }

    // ✅ Inicio de sesión con JWT
    route("/login") {
        post {
            val body = call.receive<LoginRequest>()
            val normalizedEmail = body.email.trim().lowercase()

            val userRow = transaction {
                users.selectAll()
                    .firstOrNull { it[users.email].trim().lowercase() == normalizedEmail }
            }

            if (userRow == null) {
                call.respondText("❌ Usuario no encontrado", status = HttpStatusCode.Unauthorized)
                return@post
            }

            val valid = BCrypt.verifyer().verify(body.password.toCharArray(), userRow[users.password]).verified

            if (!valid) {
                call.respondText("❌ Contraseña incorrecta", status = HttpStatusCode.Unauthorized)
                return@post
            }

            val userId = userRow[users.id].toString()
            val token = JwtConfig.generateToken(userId)
            call.respond(LoginResponse(token, 24 * 60 * 60 * 1000)) // 24 horas
        }
    }

    // 🔜 Aqui pones el manejo del jwt rafaela del futuro
}
