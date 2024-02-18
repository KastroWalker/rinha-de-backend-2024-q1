package kastro.dev.exceptions

class ResourceNotFoundException: ApplicationException(statusCode = 404)