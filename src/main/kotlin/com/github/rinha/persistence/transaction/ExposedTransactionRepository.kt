package com.github.rinha.persistence.transaction

import com.github.eu.DefaultDAO
import com.github.eu.query.order
import com.github.eu.query.pagination.Order
import com.github.eu.query.pagination.Order.DESC
import com.github.eu.util.query
import com.github.rinha.entity.Transaction
import com.github.rinha.persistence.client.ClientTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder
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
            .orderBy(TransactionTable.createdAt to DESC.order())
            .map { it.toDomain() }
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