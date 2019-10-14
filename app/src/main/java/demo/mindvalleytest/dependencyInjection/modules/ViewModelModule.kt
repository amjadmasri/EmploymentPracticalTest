package demo.mindvalleytest.dependencyInjection.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import demo.mindvalleytest.viewModels.ImagesListViewModel
import demo.mindvalleytest.dependencyInjection.interfaces.ViewModelKey
import demo.mindvalleytest.viewModels.ViewModelProviderFactory

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ImagesListViewModel::class)
    abstract fun bindImagesListViewModel(imagesListViewModel: ImagesListViewModel): ViewModel
}