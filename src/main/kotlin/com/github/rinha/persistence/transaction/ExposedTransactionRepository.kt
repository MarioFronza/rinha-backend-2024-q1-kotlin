package com.github.rinha.persistence.transaction

import com.github.eu.DefaultDAO
import com.github.eu.util.query
import com.github.rinha.entity.Transaction
import com.github.rinha.persistence.client.ClientTable
import org.jetbrains.exposed.dao.id.EntityID
import java.time.Instant

class ExposedTransactionRepository : TransactionRepository,
    DefaultDAO<Transaction, Int, TransactionEntity>(TransactionEntity) {
    override fun TransactionEntity.toDomain() = Transaction(
        id = id.value,
        clientId = clientId.value,
        value = value,
        type = type,
        createdAt = createdAt,
        description = description
    )

    override suspend fun findByClientId(clientId: Int): List<Transaction> {
        return TransactionEntity
            .find { TransactionTable.clientId eq clientId }
            .limit(10)
            .map { it.toDomain() }
    }

    override suspend fun create(entity: Transaction) = query {
        val clientIdEntityId = EntityID(entity.clientId, ClientTable)
        val newTransaction = TransactionEntity.new {
            clientId = clientIdEntityId
            type = type
            value = value
            description = description
            createdAt = Instant.now()
        }
        newTransaction.toDomain()
    }

    override suspend fun update(entity: Transaction) = query {
        TODO("Not yet implemented")
    }
}