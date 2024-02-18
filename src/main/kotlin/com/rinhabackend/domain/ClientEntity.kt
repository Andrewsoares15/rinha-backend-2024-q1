package com.rinhabackend.domain

import com.rinhabackend.resources.BalanceLimitExceededException
import jakarta.persistence.*
import kotlin.math.absoluteValue

@Entity
@Table(name = "client")
class ClientEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDT_CLIENT")
    val id: Long,

    @Column(name = "NAM_CLIENT")
    val name: String,

    @Column(name = "LIMITE")
    val limite: Long,

    @Column(name = "BALANCE")
    var balance: Long,

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    var transactions: MutableSet<TransactionEntity> = mutableSetOf()

) {
    fun addTransaction(transaction: TransactionEntity) {
        transactions = mutableSetOf(transaction)
    }

    fun addDebit(amount: Long) {
        this.balance = this.balance.minus(amount)
        if (balance.absoluteValue > limite) {
            throw BalanceLimitExceededException()
        }
    }

    fun addCredit(amount: Long) {
        this.balance = this.balance.plus(amount)
    }
}