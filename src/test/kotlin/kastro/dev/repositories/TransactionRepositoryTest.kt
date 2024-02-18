package kastro.dev.repositories

import assertk.assertThat
import assertk.assertions.isEqualTo
import kastro.dev.models.Transaction
import kotlin.test.Test

class TransactionRepositoryTest {
    private val sut = TransactionRepository()

    @Test
    fun `should create a transaction`() {
        val transaction = Transaction(
            value = 100,
            type = "credit",
            description = "Credit transaction",
            clientId = 1
        )

        val result = sut.createTransaction(transaction)

        val transactionById = getTransactionById(result)

        assertThat(transactionById.id).isEqualTo(result)
        assertThat(transactionById.value).isEqualTo(transaction.value)
        assertThat(transactionById.type).isEqualTo(transaction.type)
        assertThat(transactionById.description).isEqualTo(transaction.description)
        assertThat(transactionById.clientId).isEqualTo(transaction.clientId)
    }

    private fun getTransactionById(id: Int): Transaction {
        val connection = Database.connection

        val sql = "SELECT * FROM transacoes WHERE id = ?"
        val query = connection.prepareStatement(sql)

        query.setInt(1, id)

        val result = query.executeQuery()

        result.next()

        val transaction = Transaction(
            id = result.getInt("id"),
            value = result.getInt("valor"),
            type = result.getString("tipo"),
            description = result.getString("descricao"),
            clientId = result.getInt("cliente_id")
        )

        return transaction
    }
}