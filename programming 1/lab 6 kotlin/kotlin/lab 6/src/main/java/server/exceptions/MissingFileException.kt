package server.exceptions

/**
 * Исключение, выбрасываемое при отсутствии требуемого файла.
 */
class MissingFileException(message: String?) : RuntimeException(message)
