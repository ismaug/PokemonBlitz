package services

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.util.UUID

object scores : UUIDTable() {
    val userId = reference("user_id", users)
    val score = integer("score")
    val correctAnswers = integer("correct_answers")
    val time = integer("time")
    val createdAt = timestamp("created_at").default(Instant.now())
}

object ScoreService {
    fun insertScore(userId: UUID, score: Int, correctAnswers: Int, time: Int) {
        transaction {
            scores.insert {
                it[scores.userId] = userId
                it[scores.score] = score
                it[scores.correctAnswers] = correctAnswers
                it[scores.time] = time
                // createdAt se maneja automáticamente con el default
            }
        }
    }

    fun getScoresByUser(userId: UUID): List<ResultRow> = transaction {
        scores.select(scores.userId eq userId).toList()
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
                .map { row ->
                    GlobalScore(
                        username = row[users.username],
                        score = row[scores.score],
                        time = row[scores.time],
                        correctAnswers = row[scores.correctAnswers],
                        createdAt = formatter.format(
                            row[scores.createdAt].atZone(ZoneId.systemDefault()).toLocalDate()
                        )
                    )
                }
        }
    }
}