package com.github.rinha.plugins

import com.github.rinha.persistence.config.DatabaseConfig
import io.ktor.server.application.*

fun Application.configureDatabase() = environment.config.run {
    DatabaseConfig.hikari(
        driver = property("database.driver").getString(),
        host = property("database.host").getString(),
        schema = property("database.schema").getString(),
        dbUsername = property("database.username").getString(),
        dbPassword = property("database.password").getString()
    )
}