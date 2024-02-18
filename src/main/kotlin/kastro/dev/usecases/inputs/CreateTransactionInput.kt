package kastro.dev.usecases.inputs

data class CreateTransactionInput(
    val clientId: Int,
    val value: Int,
    val type: String,
    val description: String
)