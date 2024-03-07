package com.github.rinha.persistence.transaction

import com.github.eu.DefaultDAO
import com.github.eu.query.order
import com.github.eu.query.pagination.Order
import com.github.eu.util.query
import com.github.rinha.entity.ClientBalance
import com.github.rinha.entity.Transaction
import com.github.rinha.entity.notification.NotificationErrorType.DOMAIN_ERROR
import com.github.rinha.entity.notification.NotificationErrorType.ENTITY_NOT_FOUND
import com.github.rinha.entity.notification.NotificationOutput
import com.github.rinha.entity.notification.NotificationOutput.NotificationError
import com.github.rinha.persistence.client.ClientEntity
import com.github.rinha.persistence.utils.FunctionResponse
import com.github.rinha.persistence.utils.execAndMap
import com.github.rinha.usecase.statement.models.StatementInfoRepositoryOutput

class ExposedTransactionRepository : TransactionRepository,
    DefaultDAO<Transaction, Int, TransactionEntity>(TransactionEntity) {
    override fun TransactionEntity.toDomain() = Transaction(
        id = id.value,
        clientId = clientId.value,
        value = value,
        type = typeChar,
        createdAt = createdAt,
        description = description
    )

    override suspend fun findByClientId(clientId: Int) = query {
        val client = ClientEntity.findById(clientId) ?: return@query NotificationError(
            message = "Database operation error: CLIENT_NOT_FOUND",
            type = ENTITY_NOT_FOUND
        )
        val transactions = TransactionEntity
            .find { TransactionTable.clientId eq client.id }
            .limit(10)
            .order(TransactionTable, null, TransactionTable.createdAt to Order.DESC)
            .map { it.toDomain() }

        NotificationOutput.NotificationSuccess(
            data = StatementInfoRepositoryOutput(
                client = client.toEntity(),
                transactions = transactions
            )
        )
    }

    override suspend fun createAndUpdateClientBalance(clientId: Int, transaction: Transaction) = query {
        val query =
            "SELECT * FROM update_client_balance($clientId, ${transaction.value}, '${transaction.type}', '${transaction.description}');"
        val response = query.execAndMap { rs ->
            FunctionResponse(
                isError = rs.getBoolean("is_error"),
                message = rs.getString("text_message"),
                data = ClientBalance(
                    balance = rs.getInt("updated_balance"),
                    limit = rs.getInt("client_limit"),
                )
            )
        }.first()
        if (response.isError) {
            return@query NotificationError(
                message = "Database operation error: ${response.message}",
                type = if (response.message == "CLIENT_NOT_FOUND") {
                    ENTITY_NOT_FOUND
                } else {
                    DOMAIN_ERROR
                }
            )
        }
        NotificationOutput.NotificationSuccess(
            data = response.data
        )
    }

    override suspend fun create(entity: Transaction) = query {
        TODO("Not yet implemented")
    }

    override suspend fun update(entity: Transaction) = query {
        TODO("Not yet implemented")
    }


}

