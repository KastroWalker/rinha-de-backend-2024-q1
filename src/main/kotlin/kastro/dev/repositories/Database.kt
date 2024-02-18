package kastro.dev.repositories

import java.sql.Connection
import java.sql.DriverManager

object Database {
    // TODO : Get data from env
    private const val JDBC_URL = "jdbc:postgresql://localhost:5433/transactiondb"
    val connection: Connection = DriverManager.getConnection(JDBC_URL, "postgres", "postgres")
}