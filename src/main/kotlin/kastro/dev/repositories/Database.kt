package kastro.dev.repositories

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

object Database {
    private val hikariDataSource: HikariDataSource

    init {
        val config = HikariConfig()
        config.jdbcUrl = System.getenv("DATABASE_URL")
        config.username = "postgres"
        config.password = "postgres"
        config.maximumPoolSize = 5

        hikariDataSource = HikariDataSource(config)
    }

    val connection: Connection
        get() = hikariDataSource.connection
}