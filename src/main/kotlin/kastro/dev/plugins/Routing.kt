package kastro.dev.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kastro.dev.exceptions.ApplicationException
import kastro.dev.plugins.requests.TransactionRequest
import kastro.dev.plugins.responses.TransactionResponse
import kastro.dev.usecases.CreateTransactionUseCase
import kastro.dev.usecases.inputs.CreateTransactionInput

val createTransactionUseCase = CreateTransactionUseCase()

// TODO : Implement integration test for this route

fun Application.configureRouting() {
    routing {
        post("/clientes/{id}/transacoes") {
            try {
                val clientId = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val transaction = call.receive<TransactionRequest>()
                val input = CreateTransactionInput(
                    clientId = clientId.toInt(),
                    value = transaction.value,
                    type = transaction.type,
                    description = transaction.description
                )
                val output = createTransactionUseCase.execute(input)
                val response = TransactionResponse(
                    limit = output.limit,
                    balance = output.balance
                )
                call.respond(HttpStatusCode.OK, response)
            } catch (e: ApplicationException) {
                call.respond(e.statusCode)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
