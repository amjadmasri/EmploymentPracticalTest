package demo.mindvalleytest.dependencyInjection.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import demo.mindvalleytest.ui.ImagesListFragment

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeImagesListFragment(): ImagesListFragment
}