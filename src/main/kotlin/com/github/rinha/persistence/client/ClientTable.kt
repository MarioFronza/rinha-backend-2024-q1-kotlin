package com.github.rinha.persistence.client

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ClientTable : IntIdTable() {
    val name = varchar("name", 50).index()
    val balance = integer("balance").default(0)
    val lmt = integer("lmt")
}

class ClientEntity(id: EntityID<Int>) : IntEntity(id) {
    var name by ClientTable.name
    var balance by ClientTable.balance
    var lmt by ClientTable.lmt

    companion object : IntEntityClass<ClientEntity>(ClientTable)
}