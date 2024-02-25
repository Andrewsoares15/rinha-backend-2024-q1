package com.rinhabackend.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.LocalDateTime

class TransactionRequest(
    @JsonProperty("valor")
    @field:NotNull(message = "value must be provided")
    @field:Min(value = 1, message = "value cannot be equal or less than 0")
    val value: Int,

    @JsonProperty("descricao")
    @field:NotNull(message = "description must be provided")
    @field:Size(min = 1, max = 10, message = "description must be between 1 and 60 characters")
    val description: String?,

    @JsonProperty("tipo")
    @field:NotNull(message = "type must be provided")
    @field:NotBlank(message = "type must be provided")
    @field:ValidTransactionType(message = "type must be C or D")
    val type: String?
)

data class TransactionStatement(
    val saldo: Balance,
    val ultimas_transacoes: List<Transaction>
)

data class Transaction(
    val valor: Int,
    val tipo: TransactionType,
    val descricao: String,
    val realizada_em: LocalDateTime
)

data class Balance(
    val total: Int,
    val data_extrato: LocalDateTime = LocalDateTime.now(),
    val limite: Int
)

@Configuration
class ObjectMapperConfig {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().findAndRegisterModules()
            .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
    }
}