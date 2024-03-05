package com.github.rinha.usecase.transaction

import com.github.rinha.usecase.transaction.models.CreateTransactionInput
import com.github.rinha.usecase.transaction.models.TransactionOutput
import com.github.rinha.usecase.transaction.models.TransactionOutput.Companion.fromClientEntity
import com.github.rinha.entity.Transaction
import com.github.rinha.persistence.client.ClientRepository
import com.github.rinha.persistence.transaction.TransactionRepository

class CreateTransactionUseCase(
    private val clientRepository: ClientRepository,
    private val transactionRepository: TransactionRepository
) {

    suspend fun create(clientId: Int, request: CreateTransactionInput): TransactionOutput {
        val client = clientRepository.findById(clientId) ?: throw Exception("not found")
        transactionRepository.create(
            Transaction(
                clientId = clientId,
                value = request.valor,
                type = request.tipo,
                description = request.descricao
            )
        )
        return fromClientEntity(client)
    }

}