package com.rinhabackend.domain

import jakarta.persistence.*

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
)