package demo.mindvalleytest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import demo.mindvalleytest.data.repositories.MVImageRepository
import demo.mindvalleytest.ui.imagesList.ImagesListViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
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