package com.github.rinha.controllers.transaction

import com.github.rinha.entity.Client
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    val limite: Int,
    val saldo: Int
) {
    companion object {
        fun fromClientEntity(client: Client) = TransactionResponse(
            limite = client.limit,
            saldo = client.balance
        )
    }
}