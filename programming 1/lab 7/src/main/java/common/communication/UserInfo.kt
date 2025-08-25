package common.communication

import java.io.Serializable

data class UserInfo(val login: String, val password: String, val status: UserStatus): Serializable
