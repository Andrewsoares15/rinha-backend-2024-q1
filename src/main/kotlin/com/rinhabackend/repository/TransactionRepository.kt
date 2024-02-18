package com.rinhabackend.repository

import com.rinhabackend.domain.TransactionEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : CrudRepository<TransactionEntity, Long> {

    @Query(value = """
        SELECT t.* FROM MOVEMENTS t
        WHERE t.IDT_CLIENT= :id
        ORDER BY t.DAT_CREATION DESC
        LIMIT 10
        """, nativeQuery = true
    )
    fun findByClientId10LatestTransactions(id: Long): List<TransactionEntity>

}