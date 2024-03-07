package com.github.rinha.usecase.statement

import com.github.rinha.entity.notification.NotificationOutput
import com.github.rinha.entity.notification.NotificationOutput.NotificationError
import com.github.rinha.entity.notification.NotificationOutput.NotificationSuccess
import com.github.rinha.persistence.transaction.TransactionRepository
import com.github.rinha.usecase.statement.models.StatementOutput
import com.github.rinha.usecase.statement.models.StatementOutput.Companion.fromClientAndTransactions
import java.time.Instant

class GetStatementUseCase(
    private val transactionRepository: TransactionRepository
) {

    suspend fun getBy(clientId: Int): NotificationOutput<StatementOutput> {
        val statementOutput = when (val repositoryResponse = transactionRepository.findByClientId(clientId)) {
            is NotificationSuccess -> repositoryResponse.data
            is NotificationError -> return repositoryResponse
        }

        return NotificationSuccess(
            data = fromClientAndTransactions(
                client = statementOutput.client,
                transactions = statementOutput.transactions,
                statementDate = Instant.now()
            )
        )
    }

}