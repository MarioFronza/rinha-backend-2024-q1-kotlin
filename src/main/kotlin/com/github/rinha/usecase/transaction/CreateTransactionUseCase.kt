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

        val newBalance = calculateNewBalance(client.balance, request)

        if (isDebitTransaction(request) && !isBalanceConsistent(newBalance, client.limit)) {
            throw Exception()
        }

        val updatedClient = clientRepository.updateBalance(clientId, newBalance)
        transactionRepository.create(
            Transaction(
                clientId = clientId,
                value = request.valor,
                type = request.tipo,
                description = request.descricao
            )
        )

        return fromClientEntity(updatedClient)
    }

    private fun isDebitTransaction(request: CreateTransactionInput) = request.tipo == 'd'
    private fun isBalanceConsistent(newBalance: Int, limit: Int) = newBalance >= -limit
    private fun calculateNewBalance(currentBalance: Int, request: CreateTransactionInput): Int {
        return if (isDebitTransaction(request)) {
            currentBalance - request.valor
        } else {
            currentBalance + request.valor
        }
    }

}