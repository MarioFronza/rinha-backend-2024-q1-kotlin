package com.github.rinha.usecase.notification

import io.ktor.http.*

sealed class NotificationOutput<out T> {
    data class NotificationSuccess<out T>(val data: T) : NotificationOutput<T>()

    data class NotificationError(
        val message: String,
        val type: HttpStatusCode
    ) : NotificationOutput<Nothing>()
}