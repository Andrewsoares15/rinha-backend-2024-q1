package com.rinhabackend.resources

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ErrorHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ClientNotFoundException::class)
    fun handleClientNotFound(): ResponseEntity<Any> {
        return ResponseEntity("Client not found", NOT_FOUND)
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BalanceLimitExceededException::class)
    fun handleBalanceLimitExceeded(): ResponseEntity<Any> {
        return ResponseEntity("Balance limit exceeded", UNPROCESSABLE_ENTITY)
    }
}

class ClientNotFoundException : RuntimeException() {
    override val message: String
        get() = "Transaction not found"
}

class BalanceLimitExceededException : RuntimeException() {
    override val message: String
        get() = "Balance limit exceeded"
}