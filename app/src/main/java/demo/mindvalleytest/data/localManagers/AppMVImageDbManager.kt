package demo.mindvalleytest.data.localManagers

import androidx.paging.DataSource
import demo.mindvalleytest.data.AppDatabase
import demo.mindvalleytest.data.models.local.MvImagesLocal
import io.reactivex.Completable
import javax.inject.Inject

class AppMVImageDbManager @Inject constructor(private val appDatabase: AppDatabase) :
    MVImageDbManager {


    override fun insertMvImagesLocalList(mvImagesLocalList: List<MvImagesLocal>): Completable {
        return appDatabase.mvImageDao().insertList(mvImagesLocalList)
    }

    override fun getPagedMvImagesLocal(): DataSource.Factory<Int, MvImagesLocal> {
        return appDatabase.mvImageDao().loadPagedMVImages()
    }
}