package kastro.dev.repositories

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.ktor.http.*
import kastro.dev.exceptions.ResourceNotFoundException
import org.junit.Assert.assertThrows
import java.security.SecureRandom
import kotlin.test.Test

class ClientRepositoryTest {
    private val sut = ClientRepository()

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

        assertThat(exception.statusCode).isEqualTo(HttpStatusCode.NotFound)
    }
}