plugins {
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.ktor)
    alias(libs.plugins.flyway)
    application
    kotlin("jvm")
}

group = "com.example.pokemonblitz"
version = "1.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.call.logging)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.hikaricp)
    implementation(libs.flyway.core)

    implementation(libs.postgresql)
    implementation(libs.logback.classic)

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.ktor.server.test.host)
}
flyway {
    url = System.getenv("JDBC_DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/pokemonblitz"
    user = System.getenv("DB_USER") ?: "postgres"
    password = System.getenv("DB_PASSWORD") ?: "salem"
    locations = arrayOf("filesystem:src/main/resources/db/migration")
    driver = "org.postgresql.Driver"
}

