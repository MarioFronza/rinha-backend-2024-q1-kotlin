package com.github.rinha.controllers.transaction

import com.github.rinha.entity.Transaction
import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest(
    val tipo: String,
    val valor: Int,
    val descricao: String
) {
    fun toEntity(clientId: Int) = Transaction(
        clientId = clientId,
        type = tipo,
        value = valor,
        description = descricao
    )
}