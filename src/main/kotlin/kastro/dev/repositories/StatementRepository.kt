package kastro.dev.repositories

import kastro.dev.exceptions.ResourceNotFoundException
import kastro.dev.plugins.responses.StatementResponse

// TODO : Implement test for this class

class StatementRepository {
    fun getStatement(clientId: Int): StatementResponse {
        val connection = Database.connection

        try {
            val sql = """
                SELECT
                    c.saldo,
                    c.limite,
                    t.valor,
                    t.tipo,
                    t.descricao,
                    t.realizada_em
                FROM clientes as c
                LEFT JOIN transacoes as t ON c.id = t.cliente_id
                WHERE c.id = ?
                ORDER BY t.realizada_em DESC
                LIMIT 10
            """.trimIndent()

            val query = connection.prepareStatement(sql)

            query.setInt(1, clientId)

            val result = query.executeQuery()

            var limit: Long? = null
            var balance: Long? = null
            val transactions = mutableListOf<StatementResponse.Transaction>()

            while (result.next()) {
                limit = result.getLong("limite")
                balance = result.getLong("saldo")
                val performedAt = result.getString("realizada_em") ?: null

                if (performedAt != null) {
                    val transaction = StatementResponse.Transaction(
                        value = result.getLong("valor"),
                        type = result.getString("tipo"),
                        description = result.getString("descricao"),
                        performedAt = performedAt,
                    )

                    transactions.add(transaction)
                }
            }

            if (limit == null || balance == null) {
                throw ResourceNotFoundException()
            }

            val response = StatementResponse(
                balance = StatementResponse.Balance(
                    total = balance,
                    limit = limit,
                ),
                transactions = transactions,
            )

            return response
        } finally {
            connection.close()
        }
    }
}