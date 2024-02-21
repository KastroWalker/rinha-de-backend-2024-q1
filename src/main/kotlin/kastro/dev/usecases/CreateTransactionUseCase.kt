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
        val newBalance: Long

        if (transactionInput.type == "d") {
            newBalance = client.withDraw(transactionInput.value)

            if (-newBalance > client.limit) throw InvalidArgumentException()
        } else {
            newBalance = client.deposit(transactionInput.value)
        }

        val newTransaction = Transaction(
            value = transactionInput.value,
            type = transactionInput.type,
            description = transactionInput.description,
            clientId = transactionInput.clientId,
        )

        transactionRepository.createTransactionAndUpdateClientBalance(transaction =  newTransaction, newBalance = newBalance)

        return CreateTransactionOutput(
            limit = client.limit,
            balance = client.balance
        )
    }
}