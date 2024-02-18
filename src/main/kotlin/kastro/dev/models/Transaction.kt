package kastro.dev.models

import java.time.LocalDateTime

data class Transaction(
    val id: Int? = null,
    val value: Long,
    val type: String,
    val description: String,
    val clientId: Int,
    val realizedAt: LocalDateTime = LocalDateTime.now()
)