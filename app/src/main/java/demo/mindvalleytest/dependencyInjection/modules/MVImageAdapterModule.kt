package demo.mindvalleytest.dependencyInjection.modules

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import dagger.Module
import dagger.Provides
import demo.mindvalleytest.R
import demo.mindvalleytest.ui.imagesList.ImagesListFragment
import demo.mindvalleytest.ui.adapters.MVImagePagedAdapter
import demo.mindvalleytest.utilities.MVImageDiffCallBacks

@Module
class MVImageAdapterModule {

    @Provides
    fun provideMVImagePagedAdapter(mvImageDiffCallBacks: MVImageDiffCallBacks): MVImagePagedAdapter {
        return MVImagePagedAdapter(mvImageDiffCallBacks)
    }

    @Provides
    internal fun provideGridLayoutManager(
        imagesListFragment: ImagesListFragment,
        context: Context
    ): GridLayoutManager {
        val isPhone = context.resources.getBoolean(R.bool.is_phone)
        return if (isPhone)
            GridLayoutManager(imagesListFragment.activity, 2)
        else
            GridLayoutManager(imagesListFragment.activity, 3)
    }
}