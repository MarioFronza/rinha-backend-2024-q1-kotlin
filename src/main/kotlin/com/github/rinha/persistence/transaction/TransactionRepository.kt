package com.github.rinha.persistence.transaction

import com.github.eu.Repository
import com.github.rinha.entity.Client
import com.github.rinha.entity.ClientBalance
import com.github.rinha.entity.Transaction
import com.github.rinha.entity.notification.NotificationOutput
import com.github.rinha.usecase.statement.models.StatementInfoRepositoryOutput

interface TransactionRepository : Repository<Transaction, Int> {

    suspend fun findByClientId(clientId: Int): NotificationOutput<StatementInfoRepositoryOutput>

    suspend fun createAndUpdateClientBalance(clientId: Int, transaction: Transaction): NotificationOutput<ClientBalance>

}