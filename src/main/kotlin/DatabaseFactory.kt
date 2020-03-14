package com.github.iamthen0ise

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction


object DatabaseFactory {
    var TESTING = false

    fun init() {
        if (TESTING) {
            return
        }
        val appConfig = ConfigFactory.load()
        val dbUrl = appConfig.getString("db.DATABASE_URL")
        val dbUser = appConfig.getString("db.POSTGRES_USER")
        val dbPassword = appConfig.getString("db.POSTGRES_PASS")

        Database.connect(hikari(dbUrl, dbUser, dbPassword))
        val flyway = Flyway.configure().dataSource(dbUrl, dbUser, dbPassword).load()
        flyway.migrate()
    }

    private fun hikari(dbUrl: String, dbUser: String, dbPassword: String): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = dbUrl
        config.username = dbUser
        config.password = dbPassword
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}