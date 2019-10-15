package demo.mindvalleytest.dependencyInjection.builder

import dagger.Module
import dagger.Provides
import demo.mindvalleytest.data.repositories.AppMVImageRepository
import demo.mindvalleytest.data.repositories.MVImageRepository

@Module
class RepositoriesModule {

    @Provides
    fun provideMVImageRepository(mvImageRepository: AppMVImageRepository): MVImageRepository =
        mvImageRepository
}