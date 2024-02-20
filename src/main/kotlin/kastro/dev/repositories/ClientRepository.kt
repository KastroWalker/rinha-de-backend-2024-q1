package kastro.dev.repositories

import kastro.dev.exceptions.ResourceNotFoundException
import kastro.dev.models.Client

class ClientRepository {
    fun getById(id: Int): Client {
        val connection = Database.connection

        val sql = "SELECT * FROM clientes WHERE id = ?"
        val query = connection.prepareStatement(sql)

        query.setInt(1, id)

        val result = query.executeQuery()

        if (result.next()) {
            return Client.restore(
                id = result.getInt("id"),
                name = result.getString("nome"),
                limit = result.getLong("limite"),
                balance = result.getLong("saldo")
            )
        }

        throw ResourceNotFoundException()
    }
}