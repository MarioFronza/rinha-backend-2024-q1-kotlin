package com.github.rinha.entity

import com.github.rinha.entity.notification.NotificationErrorType.DATA_VALIDATION_ERROR
import com.github.rinha.entity.notification.NotificationOutput
import com.github.rinha.entity.notification.NotificationOutput.NotificationError
import com.github.rinha.entity.notification.NotificationOutput.NotificationSuccess
import java.time.Instant

data class Transaction(
    val id: Int = -1,
    val clientId: Int,
    val value: Int,
    val type: Char,
    val description: String,
    val createdAt: Instant? = null,
) {
    fun isValid(): NotificationOutput<Transaction> {
        if (value <= 0) return validationError("Invalid value")
        if (type != 'c' && type != 'd') return validationError("Invalid type")
        if (description.length !in 1..10) return validationError("Invalid description")
        return NotificationSuccess(this)
    }

    private fun validationError(message: String) = NotificationError(
        message = message,
        type = DATA_VALIDATION_ERROR
    )
}