package com.github.rinha.usecase.transaction

import com.github.rinha.entity.notification.NotificationOutput
import com.github.rinha.entity.notification.NotificationOutput.NotificationError
import com.github.rinha.entity.notification.NotificationOutput.NotificationSuccess
import com.github.rinha.persistence.transaction.TransactionRepository
import com.github.rinha.usecase.transaction.models.CreateTransactionInput
import com.github.rinha.usecase.transaction.models.TransactionOutput
import com.github.rinha.usecase.transaction.models.TransactionOutput.Companion.fromClientBalance

class CreateTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {

    suspend fun create(clientId: Int, request: CreateTransactionInput): NotificationOutput<TransactionOutput> {
        val notificationOutput = request.toEntity(clientId).run { isValid() }

        val transaction = when (notificationOutput) {
            is NotificationSuccess -> notificationOutput.data
            is NotificationError -> return notificationOutput
        }

        val repositoryResponse = transactionRepository.createAndUpdateClientBalance(clientId, transaction)

        val updatedBalance = when (repositoryResponse) {
            is NotificationSuccess -> repositoryResponse.data
            is NotificationError -> return repositoryResponse
        }

        return NotificationSuccess(
            data = fromClientBalance(updatedBalance)
        )
    }

}
