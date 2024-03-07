package com.github.rinha.persistence.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig

object DatabaseConfig {

    fun hikari(
        driver: String,
        host: String,
        schema: String,
        dbUsername: String,
        dbPassword: String,
    ) {
        val url = "jdbc:postgresql://${host}:5432/" + schema
        val config = HikariConfig().apply {
            jdbcUrl = url
            driverClassName = driver
            username = dbUsername
            password = dbPassword
            maximumPoolSize = 20
            isAutoCommit = false
            connectionTimeout = 250
            maxLifetime = 600000
            minimumIdle = 10
        }
        val ds = HikariDataSource(config)
        Database.connect(
            datasource = ds,
            databaseConfig = DatabaseConfig {
                defaultRepetitionAttempts = 3
            }
        )
    }

}