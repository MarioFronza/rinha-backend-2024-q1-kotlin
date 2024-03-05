package com.github.rinha.usecase.statement.models

import com.github.rinha.entity.Transaction
import kotlinx.serialization.Serializable

@Serializable
data class StatementTransactionOutput(
    val valor: Int,
    val tipo: String,
    val descricao: String,
    val realizada_em: String
) {
    companion object {
        fun fromTransactionEntity(entity: Transaction) = StatementTransactionOutput(
            valor = entity.value,
            tipo = entity.type,
            descricao = entity.description,
            realizada_em = entity.createdAt.toString()
        )
    }
}
