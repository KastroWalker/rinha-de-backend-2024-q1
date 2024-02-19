package kastro.dev.repositories

import java.sql.Connection
import java.sql.DriverManager

object Database {
    // TODO : Get data from env
    val connection: Connection = DriverManager.getConnection(System.getenv("DATABASE_URL"), "postgres", "postgres")
}