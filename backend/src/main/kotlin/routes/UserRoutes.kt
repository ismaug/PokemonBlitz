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
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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

    // ✅ Ruta protegida /me
    authenticate {
        get("/me") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("userId").asString()
            val uuid = UUID.fromString(userId)

            val userRow = transaction {
                users
                    .select(users.id, users.username, users.email)
                    .where { users.id eq uuid }
                    .firstOrNull()
            }

            if (userRow == null) {
                call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
            } else {
                call.respond(
                    mapOf(
                        "id" to userRow[users.id].toString(),
                        "username" to userRow[users.username],
                        "email" to userRow[users.email]
                    )
                )
            }
        }
    }
}
