package demo.mindvalleytest.data.models.DTO

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("links")
    val links: UserLinks,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_image")
    val profileImage: ProfileImage,
    @SerializedName("username")
    val username: String
)