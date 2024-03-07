package com.github.rinha.plugins.utils

import com.github.rinha.entity.notification.NotificationErrorType
import com.github.rinha.entity.notification.NotificationErrorType.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

fun NotificationErrorType.toHttpStatus() = when (this) {
    DATA_VALIDATION_ERROR -> BadRequest
    ENTITY_NOT_FOUND -> HttpStatusCode.NotFound
    DOMAIN_ERROR -> HttpStatusCode.UnprocessableEntity
}

suspend fun PipelineContext<Unit, ApplicationCall>.requiredIntParameter(
    param: String,
    statusCode: HttpStatusCode
): Int? {
    val id = call.parameters[param]?.toIntOrNull()
    if (id == null) {
        call.respond(statusCode, "Invalid int parameter: $param")
        return null
    }
    return id
}

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.receiveOrRespondBadRequest(): T? {
    val input = runCatching { call.receiveNullable<T>() }
    return if (input.isSuccess) {
        input.getOrNull()
    } else {
        call.respond(BadRequest, "Failed to parse input")
        null
    }
}