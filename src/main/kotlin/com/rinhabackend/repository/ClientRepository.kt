package com.rinhabackend.repository

import com.rinhabackend.domain.ClientEntity
import org.springframework.data.repository.CrudRepository

interface ClientRepository : CrudRepository<ClientEntity, Long>