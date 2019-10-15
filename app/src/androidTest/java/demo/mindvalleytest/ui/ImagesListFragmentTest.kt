package demo.mindvalleytest.ui


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import demo.mindvalleytest.R
import demo.mindvalleytest.ui.imagesList.ImagesListFragment
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ImagesListFragmentTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testImagesListFragment() {


        launchFragmentInContainer<ImagesListFragment>()

        onView(withId(R.id.image_recycler_view)).check(matches(isDisplayed()))

    }
}