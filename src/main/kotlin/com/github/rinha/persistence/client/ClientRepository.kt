package com.github.rinha.persistence.client

import com.github.eu.Repository
import com.github.rinha.entity.Client

interface ClientRepository : Repository<Client, Int> {
    suspend fun updateBalance(id: Int, newBalance: Int): Client
}