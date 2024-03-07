package com.github.rinha.entity

data class Client(
    val id: Int = -1,
    val name: String,
    val balance: ClientBalance
)

data class ClientBalance(
    val balance: Int,
    val limit: Int
)
