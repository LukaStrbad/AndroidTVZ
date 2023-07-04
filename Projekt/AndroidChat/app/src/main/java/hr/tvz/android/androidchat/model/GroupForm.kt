package hr.tvz.android.androidchat.model

data class GroupForm(
    val name: String,
    val members: List<String>? = null
)
