package com.rinhabackend.resources

import com.rinhabackend.domain.TransactionAssembler
import com.rinhabackend.domain.TransactionRequest
import com.rinhabackend.domain.TransactionStatement
import com.rinhabackend.domain.TransactionType
import com.rinhabackend.repository.ClientRepository
import com.rinhabackend.repository.TransactionRepository
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/clientes")
class TransactionResource(
    private val transactionService: TransactionService
) {

    @PostMapping("/{id}/transacoes")
    @ResponseStatus(HttpStatus.OK)
    fun createTransaction(@RequestBody @Valid request: TransactionRequest, @PathVariable id: Long):  ResponseEntity<Saldo> {
        if (id > 5) {
            throw ClientNotFoundException()
        }
        return ResponseEntity.ok(transactionService.createTransaction(request, id))
    }

    @GetMapping("/{id}/extrato")
    @ResponseStatus(HttpStatus.OK)
    fun getTransactions(@PathVariable id: Long): ResponseEntity<TransactionStatement> {
        if (id > 5) {
            throw ClientNotFoundException()
        }
        return ResponseEntity.ok(transactionService.getTransactions(id))
    }
}

@Component
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val clientRepository: ClientRepository,
    private val transactionAssembler: TransactionAssembler
) {

    @Transactional
    fun createTransaction(request: TransactionRequest, clientId: Long): Saldo {
        val apply = clientRepository.findClientById(clientId).get().apply {
            when (request.type) {
                TransactionType.d.name -> {
                    this.addDebit(request.value)
                }

                TransactionType.c.name -> {
                    this.addCredit(request.value)
                }
            }
            this.addTransaction(transactionAssembler.toTransactionEntity(request, clientId))

            clientRepository.save(this)
        }
        return  Saldo(apply.limite, apply.balance)
    }

    fun getTransactions(clientId: Long): TransactionStatement {
        clientRepository.findById(clientId).get().let {
            val latestTransactions = transactionRepository.findByClientId10LatestTransactions(clientId)
            return transactionAssembler.toTransactionResponse(it, latestTransactions)
        }
    }
}

data class Saldo(
    val limite: Int,
    val saldo: Int,
)