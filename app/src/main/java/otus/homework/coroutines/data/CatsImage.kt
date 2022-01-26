package otus.homework.coroutines.data

import com.google.gson.annotations.SerializedName

data class CatsImage(
    @SerializedName("file")
    val url: String
)
