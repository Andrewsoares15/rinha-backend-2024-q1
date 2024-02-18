package com.rinhabackend.resources

import com.rinhabackend.domain.TransactionAssembler
import com.rinhabackend.domain.TransactionRequest
import com.rinhabackend.domain.TransactionStatement
import com.rinhabackend.domain.TransactionType
import com.rinhabackend.repository.ClientRepository
import com.rinhabackend.repository.TransactionRepository
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/clientes")
class TransactionResource(
    private val transactionService: TransactionService
) {

    @PostMapping("/{id}/transacoes")
    @ResponseStatus(HttpStatus.OK)
    fun createTransaction(@RequestBody @Valid request: TransactionRequest, @PathVariable id: Long) {
        transactionService.createTransaction(request, id);
    }

    @GetMapping("/{id}/extrato")
    @ResponseStatus(HttpStatus.OK)
    fun getTransactions(@PathVariable id: Long): ResponseEntity<TransactionStatement> {
        return ResponseEntity.ok(transactionService.getTransactions(id))
    }
}

@Component
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val clientRepository: ClientRepository,
    private val transactionAssembler: TransactionAssembler
) {

    fun createTransaction(request: TransactionRequest, clientId: Long) {
        val apply = clientRepository.findById(clientId).getOrNull()
            ?.apply {
                if (request.type == TransactionType.d.name) {
                    this.addDebit(request.value)
                } else {
                    this.addCredit(request.value)
                }
                this.addTransaction(transactionAssembler.toTransactionEntity(request, clientId))

            } ?: throw ClientNotFoundException()

        clientRepository.save(apply)
    }

    @Transactional
    fun getTransactions(clientId: Long): TransactionStatement {
        clientRepository.findById(clientId).getOrNull()?.let {
            val latestTransactions = transactionRepository.findByClientId10LatestTransactions(clientId)
            return transactionAssembler.toTransactionResponse(it, latestTransactions)
        } ?: throw ClientNotFoundException()
    }
}