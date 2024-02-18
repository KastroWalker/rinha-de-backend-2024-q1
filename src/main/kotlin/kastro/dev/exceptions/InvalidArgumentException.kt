package kastro.dev.exceptions

import io.ktor.http.*

class InvalidArgumentException : ApplicationException(statusCode = HttpStatusCode.UnprocessableEntity)