package demo.mindvalleytest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import demo.mindvalleytest.data.dao.MVImageDao
import demo.mindvalleytest.data.models.local.MvImagesLocal
import demo.mindvalleytest.utilities.roomConverters.URLRoomConverter
import demo.mindvalleytest.utilities.roomConverters.UserRoomConverter

@Database(entities = [MvImagesLocal::class], version = 1)
@TypeConverters(value = [URLRoomConverter::class, UserRoomConverter::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun mvImageDao(): MVImageDao
}

