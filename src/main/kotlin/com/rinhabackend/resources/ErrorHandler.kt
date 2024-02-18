package com.rinhabackend.resources

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.stream.Collectors

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

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(
        BindException::class
    )
    fun handleValidationException(ex: BindException): ResponseEntity<Any> {
        return ResponseEntity(UNPROCESSABLE_ENTITY)
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