package demo.mindvalleytest.data.models.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import demo.mindvalleytest.data.models.DTO.Urls
import demo.mindvalleytest.data.models.DTO.User

@Entity(tableName = "mv_images",indices = [Index(value = ["id"], unique = true)])
data class MvImagesLocal(
    @PrimaryKey var id:String,
    val color: String,
    val createdAt: String,
    val height: Int,
    val width: Int,
    val likes: Int,
    val urls: Urls,
    val user: User
    )