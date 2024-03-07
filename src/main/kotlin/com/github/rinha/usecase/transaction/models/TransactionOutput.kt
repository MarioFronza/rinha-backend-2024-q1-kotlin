package com.github.rinha.usecase.transaction.models

import com.github.rinha.entity.ClientBalance
import kotlinx.serialization.Serializable

@Serializable
data class TransactionOutput(
    val limite: Int,
    val saldo: Int
) {
    companion object {

        fun fromClientBalance(balance: ClientBalance) = TransactionOutput(
            saldo = balance.balance,
            limite = balance.limit
        )
    }
}