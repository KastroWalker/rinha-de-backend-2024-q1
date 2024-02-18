package kastro.dev.plugins.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    @SerialName("limite")
    val limit: Long,
    @SerialName("saldo")
    val balance: Long,
)
