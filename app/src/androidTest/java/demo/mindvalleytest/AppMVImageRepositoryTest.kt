package demo.mindvalleytest

import demo.mindvalleytest.data.localManagers.AppMVImageDbManager
import demo.mindvalleytest.data.remoteManagers.AppMVImageApiManager
import demo.mindvalleytest.data.repositories.AppMVImageRepository
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AppMVImageRepositoryTest {

    private lateinit var repository: AppMVImageRepository
    private val mockApiManager =mock(AppMVImageApiManager::class.java)
    private val mockDbManager=mock(AppMVImageDbManager::class.java)

    @Before
    fun init() {


        repository = AppMVImageRepository(
            mockApiManager,
            mockDbManager
        )
    }

    @Test
    fun getPagedRemotePopularMovieList() {

        repository.getPagedRemotePopularMovieList(1)

        verify(mockDbManager).getPagedMvImagesLocal()
    }
}