package kastro.dev.plugins.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest(
    @SerialName("valor")
    val value: Long,
    @SerialName("tipo")
    val type: String,
    @SerialName("descricao")
    val description: String
)