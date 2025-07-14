package services

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

// Tabla Users
object users : UUIDTable() {
    val username = varchar("username", 100)
    val email = varchar("email", 100)
    val password = varchar("password", 100)
}


object UserService {
    fun insertUser(username: String, email: String, password: String): UUID {
        return transaction {
            users.insertAndGetId {
                it[users.username] = username
                it[users.email] = email
                it[users.password] = password
            }.value
        }
    }

    fun getAllUsers(): List<ResultRow> {
        return transaction {
            users.selectAll().toList()
        }
    }
}

