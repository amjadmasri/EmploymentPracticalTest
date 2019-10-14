package demo.mindvalleytest.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import demo.mindvalleytest.data.models.local.MvImagesLocal
import demo.mindvalleytest.utilities.Resource

interface MVImageRepository {

    fun getPagedRemotePopularMovieList(page: Int): LiveData<Resource<PagedList<MvImagesLocal>>>
}