package com.rinhabackend.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Entity
@Table(name = "MOVEMENTS")
class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDT_TRANSACTION")
    val id: Long? = null,

    @Column(name = "NUM_VALUE")
    val value: Long,

    @Column(name = "NAM_TYPE")
    @Enumerated(EnumType.STRING)
    val type: TransactionType,

    @Column(name = "NAM_DESC")
    val description: String,

    @Column(name = "IDT_CLIENT")
    val clientId: Long,

    @CreationTimestamp
    @Column(name = "DAT_CREATION")
    var dateCreation: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDT_CLIENT", referencedColumnName = "IDT_CLIENT", insertable = false, updatable = false)
    val client: ClientEntity? = null
)

@Service
class TransactionAssembler {
    fun toTransactionEntity(transaction: TransactionRequest, clientId: Long): TransactionEntity {
        return TransactionEntity(
            clientId = clientId,
            value = transaction.value,
            type = TransactionType.valueOf(transaction.type!!),
            description = transaction.description!!
        )
    }

    fun toTransactionResponse(client: ClientEntity, transactions: List<TransactionEntity>): TransactionStatement {
        return TransactionStatement(
            saldo = Balance(
                total = client.balance,
                limite = client.limite
            ),
            ultimas_transacoes = transactions.map {
                Transaction(
                    valor = it.value,
                    tipo = it.type,
                    descricao = it.description,
                    realizada_em = it.dateCreation!!
                )
            }
        )
    }
}
