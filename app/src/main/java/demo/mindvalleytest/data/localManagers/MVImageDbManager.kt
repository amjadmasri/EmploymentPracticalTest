package demo.mindvalleytest.data.localManagers

import androidx.paging.DataSource
import demo.mindvalleytest.data.models.local.MvImagesLocal
import io.reactivex.Completable

interface MVImageDbManager {
    fun insertMvImagesLocalList(mvImagesLocalList: List<MvImagesLocal>): Completable
    fun getPagedMvImagesLocal(): DataSource.Factory<Int, MvImagesLocal>
}