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
}