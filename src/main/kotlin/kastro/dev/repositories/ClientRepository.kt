package kastro.dev.repositories

import kastro.dev.exceptions.ResourceNotFoundException
import kastro.dev.models.Client

class ClientRepository {
    fun updateBalance(clientId: Int, newBalance: Int) {
        val connection = Database.connection

        val sql = "UPDATE clientes SET saldo = ? WHERE id = ?"
        val query = connection.prepareStatement(sql)

        query.setInt(1, newBalance)
        query.setInt(2, clientId)

        query.executeUpdate()
    }

    fun getById(id: Int): Client {
        val connection = Database.connection

        val sql = "SELECT * FROM clientes WHERE id = ?"
        val query = connection.prepareStatement(sql)

        query.setInt(1, id)

        val result = query.executeQuery()

        if (result.next()) {
            return Client(
                id = result.getInt("id"),
                name = result.getString("nome"),
                limit = result.getInt("limite"),
                balance = result.getInt("saldo")
            )
        }

        throw ResourceNotFoundException()
    }
}