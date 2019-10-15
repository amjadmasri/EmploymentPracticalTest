package demo.mindvalleytest.dependencyInjection.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import demo.mindvalleytest.dependencyInjection.modules.MVImageAdapterModule
import demo.mindvalleytest.ui.JsonFileDetails.JsonFileFragment
import demo.mindvalleytest.ui.imagesList.ImagesListFragment

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(modules = [MVImageAdapterModule::class])
    abstract fun contributeImagesListFragment(): ImagesListFragment

    @ContributesAndroidInjector
    abstract fun contributeJsonFileFragment(): JsonFileFragment
}