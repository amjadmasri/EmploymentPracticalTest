package demo.mindvalleytest.utilities.roomConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import demo.mindvalleytest.data.models.DTO.User

class UserRoomConverter {

    @TypeConverter
    fun fromString(value: String): User {
        return Gson().fromJson(value, User::class.java)
    }

    @TypeConverter
    fun fromList(user: User): String {
        return Gson().toJson(user)
    }
}