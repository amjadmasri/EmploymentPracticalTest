package demo.mindvalleytest.data.models.DTO

import com.google.gson.annotations.SerializedName

data class ImageLinks(
    @SerializedName("download")
    val download: String,
    @SerializedName("html")
    val html: String,
    @SerializedName("self")
    val self: String
)