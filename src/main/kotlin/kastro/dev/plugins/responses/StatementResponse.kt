package kastro.dev.plugins.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class StatementResponse(
    @SerialName("saldo")
    val balance: Balance,
    @SerialName("ultimas_transacoes")
    val transactions: List<Transaction>,
) {
    @Serializable
    data class Balance(
        val total: Long,
        // TODO : Change this to a date type
        @SerialName("data_extrato")
        val statementDate: String = LocalDateTime.now().toString(),
        @SerialName("limite")
        val limit: Long,
    )

    @Serializable
    data class Transaction(
        @SerialName("valor")
        val value: Long,
        @SerialName("tipo")
        val type: String,
        @SerialName("descricao")
        val description: String,
        @SerialName("realizada_em")
        val performedAt: String,
    )
}