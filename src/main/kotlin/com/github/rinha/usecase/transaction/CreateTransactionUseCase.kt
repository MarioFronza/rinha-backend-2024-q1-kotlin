package com.github.rinha.usecase.transaction

import com.github.rinha.entity.Transaction
import com.github.rinha.entity.notification.NotificationErrorType.DOMAIN_ERROR
import com.github.rinha.entity.notification.NotificationErrorType.ENTITY_NOT_FOUND
import com.github.rinha.entity.notification.NotificationOutput
import com.github.rinha.entity.notification.NotificationOutput.NotificationError
import com.github.rinha.entity.notification.NotificationOutput.NotificationSuccess
import com.github.rinha.persistence.client.ClientRepository
import com.github.rinha.persistence.transaction.TransactionRepository
import com.github.rinha.usecase.transaction.models.CreateTransactionInput
import com.github.rinha.usecase.transaction.models.TransactionOutput
import com.github.rinha.usecase.transaction.models.TransactionOutput.Companion.fromClientEntity
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync

class CreateTransactionUseCase(
    private val clientRepository: ClientRepository,
    private val transactionRepository: TransactionRepository
) {

    suspend fun create(clientId: Int, request: CreateTransactionInput): NotificationOutput<TransactionOutput> {
        val notificationOutput = request.toEntity(clientId).run { isValid() }

        val transaction = when (notificationOutput) {
            is NotificationSuccess -> notificationOutput.data
            is NotificationError -> return notificationOutput
        }

        val client = clientRepository.findById(clientId) ?: return NotificationError(
            message = "Client with id $clientId was not found",
            type = ENTITY_NOT_FOUND
        )

        val newBalance = calculateNewBalance(client.balance, transaction)

        if (isDebitTransaction(transaction) && !isBalanceConsistent(newBalance, client.limit)) {
            return NotificationError(
                message = "Could not apply debit operation",
                type = DOMAIN_ERROR
            )
        }

        transactionRepository.create(transaction)
        val updatedClient = clientRepository.updateBalance(clientId, newBalance)

        return NotificationSuccess(
            data = fromClientEntity(updatedClient)
        )
    }

    private fun isBalanceConsistent(newBalance: Int, limit: Int) = newBalance >= -limit

    private fun calculateNewBalance(currentBalance: Int, transaction: Transaction): Int {
        return if (isDebitTransaction(transaction)) {
            currentBalance - transaction.value
        } else {
            currentBalance + transaction.value
        }
    }

    private fun isDebitTransaction(request: Transaction) = request.type == 'd'
}
