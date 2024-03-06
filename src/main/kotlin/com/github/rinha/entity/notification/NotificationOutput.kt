package com.github.rinha.entity.notification


sealed class NotificationOutput<out T> {
    data class NotificationSuccess<out T>(val data: T) : NotificationOutput<T>()

    data class NotificationError(
        val message: String,
        val type: NotificationErrorType
    ) : NotificationOutput<Nothing>()
}