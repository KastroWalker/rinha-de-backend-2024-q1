package kastro.dev.models

import kotlin.properties.Delegates

class Client private constructor(
    val id: Int,
    val name: String,
    val limit: Long,
) {
    var balance by Delegates.notNull<Long>()
        private set

    fun withDraw(value: Long): Long {
        balance -= value
        return balance
    }

    companion object {
        fun restore(
            id: Int,
            name: String,
            limit: Long,
            balance: Long
        ): Client {
            val client = Client(
                id = id,
                name = name,
                limit = limit
            )
            client.balance = balance
            return client
        }
    }
}
