package com.rinhabackend.repository

import com.rinhabackend.domain.ClientEntity
import jakarta.annotation.Nullable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ClientRepository : CrudRepository<ClientEntity, Long> {

    @Query(
        value = """
        SELECT c.* FROM CLIENT c
        LEFT JOIN (
            SELECT t.* FROM MOVEMENTS t
            WHERE t.IDT_CLIENT = :id
            ORDER BY t.DAT_CREATION DESC
            LIMIT 10
        ) t ON c.IDT_CLIENT = t.IDT_CLIENT
        WHERE c.IDT_CLIENT = :id
        """, nativeQuery = true
    )
    fun findClientsByIdWith10LatestTransactions(id: Long): ClientEntity?
}