package demo.mindvalleytest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import demo.mindvalleytest.data.models.local.MvImagesLocal
import demo.mindvalleytest.data.repositories.MVImageRepository
import demo.mindvalleytest.viewModels.ImagesListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ImagesListViewModelAndroidTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mvImageRepository: MVImageRepository


    @Test
    fun repositoryIsCalledOnRefreshMovies() {

        val subject = ImagesListViewModel(mvImageRepository)
        subject.getPagedMvImagesLocalList()

        verify(mvImageRepository).getPagedRemotePopularMovieList(1)
    }
}