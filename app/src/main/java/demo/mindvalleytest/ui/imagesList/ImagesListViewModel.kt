package demo.mindvalleytest.ui.imagesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import demo.mindvalleytest.data.models.local.MvImagesLocal
import demo.mindvalleytest.data.repositories.MVImageRepository
import demo.mindvalleytest.utilities.Resource
import javax.inject.Inject

class ImagesListViewModel @Inject constructor(private val mvImageRepository: MVImageRepository) :
    ViewModel() {

    private var pagedMvImagesLocalList: LiveData<Resource<PagedList<MvImagesLocal>>> =
        MutableLiveData<Resource<PagedList<MvImagesLocal>>>()

    fun getPagedMvImagesLocalList(): LiveData<Resource<PagedList<MvImagesLocal>>> {
        pagedMvImagesLocalList = mvImageRepository.getPagedRemotePopularMovieList(1)

        return pagedMvImagesLocalList
    }

}
