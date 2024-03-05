package com.github.rinha.persistence.client

import com.github.eu.DefaultDAO
import com.github.eu.Repository
import com.github.eu.util.query
import com.github.rinha.entity.Client

class ExposedClientRepository : ClientRepository, DefaultDAO<Client, Int, ClientEntity>(ClientEntity) {
    override fun ClientEntity.toDomain() = Client(
        id = id.value,
        name = name,
        balance = balance,
        limit = lmt
    )

    override suspend fun updateBalance(id: Int, newBalance: Int) = query {
        val client = ClientEntity[id].apply {
            balance = newBalance
        }
        client.toDomain()
    }

    override suspend fun create(entity: Client): Client {
        TODO("Not yet implemented")
    }

    override suspend fun update(entity: Client): Client {
        TODO("Not yet implemented")
    }
}