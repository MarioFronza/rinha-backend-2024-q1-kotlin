package com.github.rinha.persistence.transaction

import com.github.eu.Repository
import com.github.rinha.entity.Transaction

interface TransactionRepository : Repository<Transaction, Int> {

    suspend fun findByClientId(clientId: Int): List<Transaction>
}