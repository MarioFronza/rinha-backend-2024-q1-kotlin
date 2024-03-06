package com.github.rinha.persistence.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

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
            maximumPoolSize = 10
        }
        val ds = HikariDataSource(config)
        Database.connect(ds)
    }

}