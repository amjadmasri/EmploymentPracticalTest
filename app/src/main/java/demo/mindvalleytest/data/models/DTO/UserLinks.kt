package demo.mindvalleytest.data.models.DTO

import com.google.gson.annotations.SerializedName

data class UserLinks(
    @SerializedName("html")
    val html: String, @SerializedName("likes")
    val likes: String, @SerializedName("photos")
    val photos: String, @SerializedName("self")
    val self: String
)