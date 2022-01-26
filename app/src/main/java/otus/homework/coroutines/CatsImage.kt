package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatsImage(
    @SerializedName("file")
    val url: String
)
