package com.rinhabackend.resources

import com.rinhabackend.domain.*
import com.rinhabackend.repository.ClientRepository
import com.rinhabackend.repository.TransactionRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpStatusCodeException
import kotlin.jvm.optionals.getOrNull

@RestController
class TransactionResource(
    private val transactionService: TransactionService
) {

    @PostMapping("/clientes/{clienteId}/transacoes")
    @ResponseStatus(HttpStatus.OK)
    fun createTransaction(@RequestBody request: TransactionRequest, @PathVariable clienteId: Long) {
        transactionService.createTransaction(request, clienteId);
    }

    @GetMapping("/clientes/{clienteId}/extrato")
    @ResponseStatus(HttpStatus.OK)
    fun getTransactions(@PathVariable clienteId: Long): TransactionStatement {
        return transactionService.getTransactions(clienteId)
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
                if (request.type == TransactionType.d) {
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