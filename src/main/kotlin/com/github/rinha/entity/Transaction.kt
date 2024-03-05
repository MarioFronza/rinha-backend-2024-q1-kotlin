package com.github.rinha.entity

import java.time.Instant

data class Transaction(
    val id: Int = -1,
    val clientId: Int,
    val value: Int,
    val type: Char,
    val description: String,
    val createdAt: Instant? = null,
)