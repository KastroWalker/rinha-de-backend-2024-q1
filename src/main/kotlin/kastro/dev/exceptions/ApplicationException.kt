package kastro.dev.exceptions

open class ApplicationException(
    val statusCode: Int = 500,
) : Exception()