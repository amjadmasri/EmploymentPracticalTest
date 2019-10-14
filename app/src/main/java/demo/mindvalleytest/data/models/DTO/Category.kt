package demo.mindvalleytest.data.models.DTO

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("links")
    val links: CategoryLinks,
    @SerializedName("photo_count")
    val photoCount: Int,
    @SerializedName("title")
    val title: String
)