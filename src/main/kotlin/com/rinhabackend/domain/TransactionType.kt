package com.rinhabackend.domain

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

enum class TransactionType(value: String) {
    d("DEBIT"), c("CREDIT");

    companion object {
        fun isValid(value: String): Boolean {
            return entries.any { it.name == value }
        }
    }
}

class TransactionTypeValidator : ConstraintValidator<ValidTransactionType, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value != null && TransactionType.isValid(value)
    }
}

@Constraint(validatedBy = [TransactionTypeValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidTransactionType(
    val message: String = "Invalid transaction type",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)