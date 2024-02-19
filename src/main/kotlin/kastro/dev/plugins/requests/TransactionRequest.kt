package kastro.dev.plugins.requests

import kastro.dev.exceptions.InvalidArgumentException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest(
    @SerialName("valor")
    val value: Double?,
    @SerialName("tipo")
    val type: String?,
    @SerialName("descricao")
    val description: String?
) {
    fun validate() {
        validateValue()
        validateType()
        validateDescription()
    }

    private fun validateValue() {
        if (value == null) throw InvalidArgumentException()
        if (value < 0) throw InvalidArgumentException()
        if (value % 1 != 0.0) throw InvalidArgumentException()
    }

    private fun validateType() {
        if (type == null) throw InvalidArgumentException()
        require(type == "d" || type == "c") { throw InvalidArgumentException() }
    }

    private fun validateDescription() {
        if (description == null) throw InvalidArgumentException()
        require(description.length in 1..10) { throw InvalidArgumentException() }
    }
}