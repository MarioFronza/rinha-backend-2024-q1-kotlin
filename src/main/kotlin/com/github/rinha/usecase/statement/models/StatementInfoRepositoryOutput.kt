package com.github.rinha.usecase.statement.models

import com.github.rinha.entity.Client
import com.github.rinha.entity.Transaction

data class StatementInfoRepositoryOutput(
    val client: Client,
    val transactions: List<Transaction>
)