package com.github.rinha.usecase.statement.models

import com.github.rinha.entity.Client
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ClientReportOutput(
    val total: Int,
    val data_extrato: String,
    val limite: Int
) {

    companion object {
        fun fromClientEntity(entity: Client, statementDate: Instant) = ClientReportOutput(
            total = entity.balance.balance,
            data_extrato = statementDate.toString(),
            limite = entity.balance.limit
        )
    }

}
