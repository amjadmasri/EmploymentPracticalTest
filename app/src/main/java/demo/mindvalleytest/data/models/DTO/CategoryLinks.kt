package demo.mindvalleytest.data.models.DTO

import com.google.gson.annotations.SerializedName

data class CategoryLinks(
    @SerializedName("photos")
    val photos: String,
    @SerializedName("self")
    val self: String
)