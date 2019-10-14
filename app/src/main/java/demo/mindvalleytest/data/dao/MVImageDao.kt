package demo.mindvalleytest.data.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import demo.mindvalleytest.data.models.local.MvImagesLocal
import io.reactivex.Completable

@Dao
interface MVImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(imagesLocal: List<MvImagesLocal>): Completable

    @Query("SELECT * from mv_images ")
    fun loadPagedMVImages(): DataSource.Factory<Int, MvImagesLocal>

    @Query("SELECT * from mv_images ")
    fun loadMVImages(): List<MvImagesLocal>
}