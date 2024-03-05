package com.github.rinha

import com.github.rinha.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)


fun Application.module() {
    configureRouting()
}