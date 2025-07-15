
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*

object JwtConfig {
    private const val secret = "Password"
    private const val issuer = "pokemonblitz"
    private const val audience = "users"
    private const val validityInMs = 24 * 60 * 60 * 1000L // 24 horas

    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(userId: String): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", userId)
            .withExpiresAt(java.util.Date(System.currentTimeMillis() + validityInMs))
            .sign(algorithm)
    }

    fun configureKtorFeature(app: Application) {
        app.install(io.ktor.server.auth.Authentication) {
            jwt {
                realm = "PokemonBlitz"
                verifier(JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build())
                validate { credential ->
                    if (credential.payload.getClaim("userId").asString() != null) JWTPrincipal(credential.payload) else null
                }
            }
        }
    }
}
