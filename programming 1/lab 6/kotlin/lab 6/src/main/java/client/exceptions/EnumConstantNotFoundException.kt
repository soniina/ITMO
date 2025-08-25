package client.exceptions

/**
 * Исключение, выбрасываемое при неудачной попытке получить константу перечисления.
 */
class EnumConstantNotFoundException(message: String?) : RuntimeException(message)
