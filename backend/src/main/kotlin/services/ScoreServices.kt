package services

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SortOrder


object scores : UUIDTable() {
    val userId = reference("user_id", users)
    val score = integer("score")
    val correctAnswers = integer("correct_answers")
    val time = integer("time")
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
    fun getTop5Scores(): List<Triple<String, Int, Int>> {
        return transaction {
            scores
                .innerJoin(users, { scores.userId }, { users.id })
                .selectAll()
                .orderBy(scores.score to SortOrder.DESC, scores.time to SortOrder.ASC)
                .limit(5)
                .map {
                    Triple(
                        it[users.username],
                        it[scores.score],
                        it[scores.time]
                    )
                }
        }
    }
}



