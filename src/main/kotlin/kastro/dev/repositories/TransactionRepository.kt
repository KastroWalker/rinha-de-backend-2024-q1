package kastro.dev.repositories

import kastro.dev.models.Transaction
import java.time.LocalDateTime

class TransactionRepository {
    fun createTransactionAndUpdateClientBalance(transaction: Transaction, newBalance: Long) {
        val connection = Database.connection

        try {

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
        } finally {
            connection.close()
        }
    }
}