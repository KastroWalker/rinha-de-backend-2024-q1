package kastro.dev.repositories

class ClientRepository {
    fun updateBalance(clientId: Int, newBalance: Int) {
        val connection = Database.connection

        val sql = "UPDATE clientes SET saldo = ? WHERE id = ?"
        val query = connection.prepareStatement(sql)

        query.setInt(1, newBalance)
        query.setInt(2, clientId)

        query.executeUpdate()
    }
}