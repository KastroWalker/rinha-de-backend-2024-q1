package kastro.dev.usecases.inputs

data class CreateTransactionInput(
    val clientId: Int,
    val value: Long,
    val type: String,
    val description: String
)