package kastro.dev.exceptions

import io.ktor.http.*

open class ApplicationException(
    val statusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
) : Exception()