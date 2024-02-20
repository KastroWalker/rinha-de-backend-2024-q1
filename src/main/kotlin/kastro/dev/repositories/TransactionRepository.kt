package kastro.dev.repositories

import kastro.dev.models.Transaction
import java.time.LocalDateTime

class TransactionRepository {
    fun createTransaction(transaction: Transaction): Int {
        val connection = Database.connection

        val sql = "INSERT INTO transacoes (valor, tipo, descricao, realizada_em, cliente_id) VALUES (?, ?, ?, ?, ?)"
        val query = connection.prepareStatement(sql, arrayOf("id"))

        query.setLong(1, transaction.value)
        query.setString(2, transaction.type)
        query.setString(3, transaction.description)
        query.setTimestamp(4, java.sql.Timestamp.valueOf(LocalDateTime.now()))
        query.setInt(5, transaction.clientId)

        val affectedRows = query.executeUpdate()

        // TODO : Add a test to cover this case
        if (affectedRows == 0) {
            throw Exception()
        }

        val result = query.generatedKeys

        if (result.next()) {
            return result.getInt(1)
        }

        // TODO : Add a test to cover this case
        throw Exception()
    }

    fun createTransactionAndUpdateClientBalance(transaction: Transaction, newBalance: Long) {
        val connection = Database.connection

        val sql =
                "BEGIN;" +
                "UPDATE clientes SET saldo = ? WHERE id = ?;" +
                "INSERT INTO transacoes (valor, tipo, descricao, realizada_em, cliente_id) VALUES (?, ?, ?, ?, ?);" +
                "COMMIT;"
        val query = connection.prepareStatement(sql)

        query.setLong(1, newBalance)
        query.setInt(2, transaction.clientId)
        query.setLong(3, transaction.value)
        query.setString(4, transaction.type)
        query.setString(5, transaction.description)
        query.setTimestamp(6, java.sql.Timestamp.valueOf(LocalDateTime.now()))
        query.setInt(7, transaction.clientId)

        query.executeUpdate()
    }
}