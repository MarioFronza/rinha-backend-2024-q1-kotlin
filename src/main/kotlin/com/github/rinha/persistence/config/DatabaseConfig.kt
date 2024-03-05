package com.github.rinha.persistence.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {

    fun hikari(
        driver: String,
        url: String,
        schema: String,
        dbUsername: String,
        dbPassword: String,
    ) {
        val config = HikariConfig().apply {
            jdbcUrl = url + schema
            driverClassName = driver
            username = dbUsername
            password = dbPassword
            maximumPoolSize = 20
            minimumIdle = 10
        }
        val ds = HikariDataSource(config)
        Database.connect(ds)
    }

}