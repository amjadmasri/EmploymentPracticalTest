package demo.mindvalleytest

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import demo.mindvalleytest.data.AppDatabase
import demo.mindvalleytest.data.models.DTO.ProfileImage
import demo.mindvalleytest.data.models.DTO.Urls
import demo.mindvalleytest.data.models.DTO.User
import demo.mindvalleytest.data.models.DTO.UserLinks
import demo.mindvalleytest.data.models.local.MvImagesLocal
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.ArrayList

@RunWith(AndroidJUnit4::class)
class AppMVImageDbManagerTest{

    private lateinit var appDatabase: AppDatabase

    @Before
    fun init() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun uninit() {
        appDatabase.close()
    }

    @Test
    fun testLoadPopularArticles() {
        val mvImagesLocalEntities = ArrayList<MvImagesLocal>()
        val entity = createMVImage()

        mvImagesLocalEntities.add(entity)
        appDatabase.mvImageDao().insertList(mvImagesLocalEntities)
            .subscribe()
        val articlesList = appDatabase.mvImageDao().loadMVImages()
        assertEquals(1,articlesList.size)
    }

    private fun createMVImage(): MvImagesLocal {
        return MvImagesLocal(TestUtil.randomString(),TestUtil.randomString(),TestUtil.randomString(),TestUtil.randomIntNumber(),TestUtil.randomIntNumber(),TestUtil.randomIntNumber(),createUrls(),createUser())
    }

    private fun createUser(): User {
        return User(TestUtil.randomString(),createLinks(),TestUtil.randomString(),createProfileImage(),TestUtil.randomString())
    }

    private fun createProfileImage(): ProfileImage {
        return ProfileImage(TestUtil.randomString(),TestUtil.randomString(),TestUtil.randomString())
    }

    private fun createLinks(): UserLinks {
        return UserLinks(TestUtil.randomString(),TestUtil.randomString(),TestUtil.randomString(),TestUtil.randomString())
    }

    private fun createUrls(): Urls {
        return Urls(TestUtil.randomString(),TestUtil.randomString(),TestUtil.randomString(),TestUtil.randomString(),TestUtil.randomString())
    }
}