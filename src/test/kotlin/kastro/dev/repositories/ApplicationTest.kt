package kastro.dev.repositories

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kastro.dev.models.Transaction
import kastro.dev.plugins.configureRouting
import kastro.dev.plugins.configureSerialization
import kastro.dev.plugins.requests.TransactionRequest
import kastro.dev.plugins.responses.TransactionResponse
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import kotlin.test.*

class ApplicationTest {
    @Test
    fun `should create a credit transaction`() = testApplication {
        restoreDatabase()
        application {
            configureRouting()
            configureSerialization()
        }
        val clientId = 1
        val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
        val response = client.post("/clientes/$clientId/transacoes") {
            contentType(ContentType.Application.Json)
            setBody(
                TransactionRequest(
                    value = 3.0,
                    type = "c",
                    description = "desc"
                )
            )
        }
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        val bodyObject = Json.decodeFromString<TransactionResponse>(response.bodyAsText())
        assertThat(bodyObject.limit).isEqualTo(100000)
        assertThat(bodyObject.balance).isEqualTo(3)

        val transactionStored = getTransactionByClientId(clientId)

        assertThat(transactionStored.id).isEqualTo(1)
        assertThat(transactionStored.type).isEqualTo("c")
        assertThat(transactionStored.value).isEqualTo(3)
        assertThat(transactionStored.clientId).isEqualTo(clientId)
        assertThat(transactionStored.description).isEqualTo("desc")
        assertThat(transactionStored.realizedAt.toLocalDate()).isEqualTo(LocalDateTime.now().toLocalDate())
    }

    @Test
    fun `should create a debit transaction`() = testApplication {
        restoreDatabase()
        application {
            configureRouting()
            configureSerialization()
        }
        val clientId = 1
        val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
        val response = client.post("/clientes/$clientId/transacoes") {
            contentType(ContentType.Application.Json)
            setBody(
                TransactionRequest(
                    value = 3.0,
                    type = "d",
                    description = "desc"
                )
            )
        }
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        val bodyObject = Json.decodeFromString<TransactionResponse>(response.bodyAsText())
        assertThat(bodyObject.limit).isEqualTo(100000)
        assertThat(bodyObject.balance).isEqualTo(-3)

        val transactionStored = getTransactionByClientId(clientId)

        assertThat(transactionStored.id).isEqualTo(1)
        assertThat(transactionStored.type).isEqualTo("d")
        assertThat(transactionStored.value).isEqualTo(3)
        assertThat(transactionStored.clientId).isEqualTo(clientId)
        assertThat(transactionStored.description).isEqualTo("desc")
        assertThat(transactionStored.realizedAt.toLocalDate()).isEqualTo(LocalDateTime.now().toLocalDate())
    }

    private fun getTransactionByClientId(clientId: Int): Transaction {
        val connection = Database.connection

        val sql = "SELECT * FROM transacoes WHERE id = ?"
        val query = connection.prepareStatement(sql)

        query.setInt(1, clientId)

        val result = query.executeQuery()

        result.next()

        val transaction = Transaction(
            id = result.getInt("id"),
            value = result.getLong("valor"),
            type = result.getString("tipo"),
            description = result.getString("descricao"),
            clientId = result.getInt("cliente_id"),
            realizedAt = result.getTimestamp("realizada_em").toLocalDateTime()
        )

        return transaction
    }

    private fun restoreDatabase() {
        val sql = """
DROP TABLE IF EXISTS clientes CASCADE;

DROP TABLE IF EXISTS transacoes;

CREATE TABLE IF NOT EXISTS clientes(
	id SERIAL PRIMARY KEY,
	nome VARCHAR NOT NULL,
	limite INT NOT NULL,
	saldo INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transacoes(
	id SERIAL PRIMARY KEY,
	valor INT NOT NULL,
	tipo VARCHAR NOT NULL,
	descricao VARCHAR NOT NULL,
	realizada_em TIMESTAMP NOT NULL,
	cliente_id INT NOT NULL,
	FOREIGN KEY(cliente_id) REFERENCES clientes(id)
);

DO $$
BEGIN
  INSERT INTO clientes(nome, limite)
  VALUES
    ('o barato sai caro', 1000 * 100),
    ('zan corp ltda', 800 * 100),
    ('les cruders', 10000 * 100),
    ('padaria joia de cocaia', 100000 * 100),
    ('kid mais', 5000 * 100);
END; $$
        """.trimIndent()

        val connection = Database.connection
        val query = connection.prepareStatement(sql)

        query.execute()
    }
}