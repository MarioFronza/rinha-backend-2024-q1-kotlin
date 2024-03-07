package com.github.rinha.persistence.transaction

import com.github.eu.DefaultDAO
import com.github.eu.query.order
import com.github.eu.query.pagination.Order
import com.github.eu.util.query
import com.github.rinha.entity.ClientBalance
import com.github.rinha.entity.Transaction
import com.github.rinha.entity.notification.NotificationErrorType
import com.github.rinha.entity.notification.NotificationOutput
import com.github.rinha.persistence.client.ClientTable
import com.github.rinha.persistence.utils.FunctionResponse
import com.github.rinha.persistence.utils.execAndMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.selects.select
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.postgresql.jdbc.PgResultSet
import java.time.Instant

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
        TransactionEntity
            .find { TransactionTable.clientId eq clientId }
            .limit(10)
            .orderBy(TransactionTable.createdAt to Order.DESC.order())
            .map { it.toDomain() }
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
            return@query NotificationOutput.NotificationError(
                message = "Database operation error: ${response.message}",
                type = if (response.message == "CLIENT_NOT_FOUND") {
                    NotificationErrorType.ENTITY_NOT_FOUND
                } else {
                    NotificationErrorType.DOMAIN_ERROR
                }
            )
        }
        NotificationOutput.NotificationSuccess(
            data = response.data
        )
    }

    override suspend fun create(entity: Transaction) = query {
        val clientIdEntityId = EntityID(entity.clientId, ClientTable)
        val newTransaction = TransactionEntity.new {
            clientId = clientIdEntityId
            typeChar = entity.type
            value = entity.value
            description = entity.description
            createdAt = Instant.now()
        }
        newTransaction.toDomain()
    }

    override suspend fun update(entity: Transaction) = query {
        TODO("Not yet implemented")
    }


}

