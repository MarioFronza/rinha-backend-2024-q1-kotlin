package com.github.rinha.usecase.transaction.models

import com.github.rinha.entity.Client
import kotlinx.serialization.Serializable

@Serializable
data class TransactionOutput(
    val limite: Int,
    val saldo: Int
) {
    companion object {
        fun fromClientEntity(client: Client) = TransactionOutput(
            limite = client.limit,
            saldo = client.balance
        )
    }
}