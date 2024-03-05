package com.github.rinha.persistence.transaction

import com.github.rinha.persistence.client.ClientTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestamp

object TransactionTable : IntIdTable("transactions") {
    val clientId = reference("client_id", ClientTable, onDelete = ReferenceOption.CASCADE)
    val value = integer("value")
    val type = char("type", 1)
    val createdAt = timestamp("created_at")
    val description = varchar("description", 10)
}

class TransactionEntity(id: EntityID<Int>) : IntEntity(id) {
    var clientId by TransactionTable.clientId
    var value by TransactionTable.value
    var type by TransactionTable.type
    var createdAt by TransactionTable.createdAt
    var description by TransactionTable.description

    companion object : IntEntityClass<TransactionEntity>(TransactionTable)
}