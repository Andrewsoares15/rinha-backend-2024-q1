package com.rinhabackend.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class TransactionRequest(
    @JsonProperty("valor")
    val value: Long,

    @JsonProperty("descricao")
    val description: String,

    @JsonProperty("tipo")
    val type: TransactionType
)

data class TransactionStatement(
    val balance: Balance,
    val transactions: List<Transaction>
)

data class Transaction(
    val value: Long,
    val type: TransactionType,
    val description: String,
    val datTransaction: LocalDateTime
)

data class Balance(
    val total: Long,
    val datStatement: LocalDateTime =  LocalDateTime.now(),
    val limit: Long
)

enum class TransactionType(value: String) {
    d("DEBIT"), c("CREDIT")
}