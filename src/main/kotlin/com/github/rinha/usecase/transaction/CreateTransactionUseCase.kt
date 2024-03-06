package com.github.rinha.usecase.transaction

import com.github.rinha.entity.Client
import com.github.rinha.usecase.transaction.models.CreateTransactionInput
import com.github.rinha.usecase.transaction.models.TransactionOutput
import com.github.rinha.usecase.transaction.models.TransactionOutput.Companion.fromClientEntity
import com.github.rinha.entity.Transaction
import com.github.rinha.persistence.client.ClientRepository
import com.github.rinha.persistence.transaction.TransactionRepository
import com.github.rinha.usecase.notification.NotificationOutput
import com.github.rinha.usecase.notification.NotificationOutput.NotificationError
import com.github.rinha.usecase.notification.NotificationOutput.NotificationSuccess
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class CreateTransactionUseCase(
    private val clientRepository: ClientRepository,
    private val transactionRepository: TransactionRepository
) {

    suspend fun create(clientId: Int, request: CreateTransactionInput): NotificationOutput<TransactionOutput> {
        val client = clientRepository.findById(clientId) ?: return NotificationError(
            message = "Client with id $clientId was not found",
            type = HttpStatusCode.NotFound
        )

        val newBalance = calculateNewBalance(client.balance, request)

        if (isDebitTransaction(request) && !isBalanceConsistent(newBalance, client.limit)) {
            return NotificationError(
                message = "Could not apply debit operation",
                type = HttpStatusCode.UnprocessableEntity
            )
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

        return NotificationSuccess(
            data = fromClientEntity(updatedClient)
        )
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