package com.github.rinha.entity

data class Client(
    val id: Int = -1,
    val name: String,
    val balance: Int,
    val limit: Int
)
