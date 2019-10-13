package demo.mindvalleytest.dependencyInjection.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import demo.mindvalleytest.ViewModels.ImagesListViewModel
import demo.mindvalleytest.dependencyInjection.interfaces.ViewModelKey

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ImagesListViewModel::class)
    abstract fun bindImagesListViewModel(imagesListViewModel: ImagesListViewModel): ViewModel
}