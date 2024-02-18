package kastro.dev.usecases

import kastro.dev.exceptions.InvalidArgumentException
import kastro.dev.models.Transaction
import kastro.dev.repositories.ClientRepository
import kastro.dev.repositories.TransactionRepository
import kastro.dev.usecases.inputs.CreateTransactionInput
import kastro.dev.usecases.outputs.CreateTransactionOutput

class CreateTransactionUseCase {
    private val transactionRepository = TransactionRepository()
    private val clientRepository = ClientRepository()

    fun execute(transactionInput: CreateTransactionInput): CreateTransactionOutput {
        val client = clientRepository.getById(transactionInput.clientId)

        if (transactionInput.value < 0) throw InvalidArgumentException()

        val newBalance = client.withDraw(transactionInput.value)

        if (transactionInput.type == "d" && -newBalance > client.limit) throw InvalidArgumentException()

        // TODO if throw an exception when save the transaction or the balance should revert the two actions

        val newTransaction = Transaction(
            value = transactionInput.value,
            type = transactionInput.type,
            description = transactionInput.description,
            clientId = transactionInput.clientId,
        )

        transactionRepository.createTransaction(newTransaction)
        clientRepository.updateBalance(clientId = client.id, newBalance = newBalance)

        return CreateTransactionOutput(
            limit = client.limit,
            balance = client.balance
        )
    }
}