package com.github.rinha.usecase.transaction.models

import com.github.rinha.entity.Transaction
import kotlinx.serialization.Serializable

@Serializable
data class CreateTransactionInput(
    val tipo: Char,
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