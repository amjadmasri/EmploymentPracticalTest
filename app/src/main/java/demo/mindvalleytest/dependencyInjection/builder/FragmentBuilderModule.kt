package demo.mindvalleytest.dependencyInjection.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import demo.mindvalleytest.ImagesListFragment

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeImagesListFragment(): ImagesListFragment
}