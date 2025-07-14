import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.Database
import kotlinx.coroutines.Dispatchers
import org.slf4j.LoggerFactory

object Databases {
    private val logger = LoggerFactory.getLogger("Database")

    fun connectToPostgres(config: ApplicationConfig) {
        val url = config.property("postgres.url").getString()
        val user = config.property("postgres.user").getString()
        val password = config.property("postgres.password").getString()
        val driver = config.property("postgres.driverClass").getString()

        Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
        )

        logger.info("✅ Conectado a PostgreSQL: $url")
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
