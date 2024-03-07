package com.github.rinha.usecase.transaction.models

import com.github.rinha.entity.Client
import com.github.rinha.entity.ClientBalance
import kotlinx.serialization.Serializable

@Serializable
data class TransactionOutput(
    val limite: Int,
    val saldo: Int
) {
    companion object {
        fun fromClientEntity(client: Client) = TransactionOutput(
            saldo = client.balance.balance,
            limite = client.balance.limit
        )

        fun fromClientBalance(balance: ClientBalance) = TransactionOutput(
            saldo = balance.balance,
            limite = balance.limit
        )
    }
}