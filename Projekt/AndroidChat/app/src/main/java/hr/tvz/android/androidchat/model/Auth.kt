package hr.tvz.android.androidchat.model

data class Auth(
    val username: String,
    val password: String
)

data class AuthRegister(
    val username: String,
    val password: String,
    val displayName: String
)
