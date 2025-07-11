package com.example.pokemonblitz.db

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    override val primaryKey = PrimaryKey(id)
}

object Scores : Table("scores") {
    val id = integer("id").autoIncrement()
    val user = reference("user_id", Users.id)
    val score = integer("score")
    override val primaryKey = PrimaryKey(id)
}

