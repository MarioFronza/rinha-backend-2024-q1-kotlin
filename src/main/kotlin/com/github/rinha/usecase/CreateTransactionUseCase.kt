package com.github.rinha.usecase

import com.github.rinha.controllers.transaction.TransactionRequest
import com.github.rinha.controllers.transaction.TransactionResponse
import com.github.rinha.controllers.transaction.TransactionResponse.Companion.fromClientEntity
import com.github.rinha.entity.Transaction
import com.github.rinha.persistence.client.ClientRepository
import com.github.rinha.persistence.transaction.TransactionRepository

class CreateTransactionUseCase(
    private val clientRepository: ClientRepository,
    private val transactionRepository: TransactionRepository
) {

    suspend fun createTransaction(clientId: Int, request: TransactionRequest): TransactionResponse {
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