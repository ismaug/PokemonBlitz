package services

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.ZoneId

object scores : UUIDTable() {
    val userId = reference("user_id", users)
    val score = integer("score")
    val correctAnswers = integer("correct_answers")
    val time = integer("time")

    val createdAt = timestamp("created_at")
}
object ScoreService {
    fun insertScore(userId: UUID, score: Int, correctAnswers: Int, time: Int) {
        transaction {
            scores.insert {
                it[scores.userId] = userId
                it[scores.score] = score
                it[scores.correctAnswers] = correctAnswers
                it[scores.time] = time
            }
        }
    }

    fun getScoresByUser(userId: UUID): List<ResultRow> {
        return transaction {
            val condition: Op<Boolean> = scores.userId eq EntityID(userId, users)
            scores.select(condition).toList()
        }
    }
    data class GlobalScore(
        val username: String,
        val score: Int,
        val time: Int,
        val correctAnswers: Int,
        val createdAt: String
    )
    fun getTop5Scores(): List<GlobalScore> {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return transaction {
            (scores innerJoin users)
                .selectAll()
                .orderBy(scores.score to SortOrder.DESC, scores.time to SortOrder.ASC)
                .limit(5)
                .map {
                    val createdAtInstant = it[scores.createdAt].toInstant()  // ← Convierte a Instant
                    val createdAtLocal = LocalDateTime.ofInstant(createdAtInstant, ZoneId.systemDefault()) // ← Convierte a LocalDateTime
                    val formattedDate = createdAtLocal.format(formatter)

                    GlobalScore(
                        username = it[users.username],
                        score = it[scores.score],
                        time = it[scores.time],
                        correctAnswers = it[scores.correctAnswers],
                        createdAt = formattedDate
                    )
                }
        }
    }
}



