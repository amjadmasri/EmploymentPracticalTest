package demo.mindvalleytest.utilities.roomConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import demo.mindvalleytest.data.models.DTO.Urls

class URLRoomConverter {

    @TypeConverter
    fun fromString(value: String): Urls {
        return Gson().fromJson(value, Urls::class.java)
    }

    @TypeConverter
    fun fromList(urls: Urls): String {
        return Gson().toJson(urls)
    }
}