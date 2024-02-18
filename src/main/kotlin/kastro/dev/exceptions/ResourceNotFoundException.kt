package kastro.dev.exceptions

import io.ktor.http.*

class ResourceNotFoundException: ApplicationException(statusCode = HttpStatusCode.NotFound)