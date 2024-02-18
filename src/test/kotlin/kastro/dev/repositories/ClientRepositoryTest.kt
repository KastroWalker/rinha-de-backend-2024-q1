package kastro.dev.repositories

import assertk.assertThat
import assertk.assertions.isEqualTo
import java.security.SecureRandom
import kotlin.test.Test

class ClientRepositoryTest {
    private val sut = ClientRepository()

    @Test
    fun `should update the balance of a client`() {
        val clientId = 1
        val newBalance = SecureRandom().nextInt(1000)

        sut.updateBalance(clientId, newBalance)

        val newBalanceFromDatabase = getBalanceByClientId(clientId)

        assertThat(newBalanceFromDatabase).isEqualTo(newBalance)
    }

    private fun getBalanceByClientId(id: Int): Int {
        val connection = Database.connection

        val sql = "SELECT saldo FROM clientes WHERE id = ?"
        val query = connection.prepareStatement(sql)

        query.setInt(1, id)

        val result = query.executeQuery()

        result.next()

        val balance = result.getInt("saldo")

        return balance
    }
}