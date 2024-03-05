package com.github.rinha.controllers.statement

import com.github.rinha.entity.Client
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ClientReportResponse(
    val total: Int,
    val data_extrato: String,
    val limite: Int
) {

    companion object {
        fun fromClientEntity(entity: Client, statementDate: Instant) = ClientReportResponse(
            total = entity.balance,
            data_extrato = statementDate.toString(),
            limite = entity.limit
        )
    }

}
