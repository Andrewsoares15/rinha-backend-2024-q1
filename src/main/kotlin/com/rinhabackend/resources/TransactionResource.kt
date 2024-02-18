package com.rinhabackend.resources

import com.rinhabackend.domain.*
import com.rinhabackend.repository.ClientRepository
import com.rinhabackend.repository.TransactionRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrNull

@RestController
class TransactionResource(
    private val transactionService: TransactionService
) {

    @PostMapping("/clientes/{clienteId}/transacoes")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTransaction(@RequestBody request: TransactionRequest, @PathVariable clienteId: Long) {
        transactionService.createTransaction(request, clienteId);

    }

    @GetMapping("/clientes/{clienteId}/extrato")
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
                    this.balance = this.balance.minus(request.value)
                } else {
                    this.balance = this.balance.plus(request.value)
                }
                this.transactions = mutableSetOf(transactionAssembler.toTransactionEntity(request, clientId))

            } ?: throw Exception("Cliente não encontrado")

        clientRepository.save(apply)
    }

    fun getTransactions(clientId: Long): TransactionStatement {
        return transactionAssembler.toTransactionResponse(transactionRepository.findClientsByIdWith10LatestTransactions(clientId))
    }
}