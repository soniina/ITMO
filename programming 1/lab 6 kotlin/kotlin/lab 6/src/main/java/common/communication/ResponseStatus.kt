package common.communication

enum class ResponseStatus {
    SUCCESS,
    INTERMEDIATE_SUCCESS,
    ERROR,
    INTERMEDIATE_ERROR,
    ASK_OBJECT,
    HISTORY_OVERFLOW,
    EXIT
}
