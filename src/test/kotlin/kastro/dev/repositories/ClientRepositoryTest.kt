package kastro.dev.repositories

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kastro.dev.exceptions.ResourceNotFoundException
import org.junit.Assert.assertThrows
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

    @Test
    fun `should get a client by id`() {
        val clientId = 1

        val client = sut.getById(clientId)

        assertThat(client.id).isEqualTo(clientId)
    }

    @Test
    fun `should throw ResourceNotFoundException when the client does not exist`() {
        val clientId = 6

        val exception = assertThrows(ResourceNotFoundException::class.java) {
            sut.getById(clientId)
        }

        assertThat(exception.statusCode).isEqualTo(404)
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