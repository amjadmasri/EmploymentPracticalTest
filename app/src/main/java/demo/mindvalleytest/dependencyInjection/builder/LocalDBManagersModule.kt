package demo.mindvalleytest.dependencyInjection.builder

import dagger.Module
import dagger.Provides
import demo.mindvalleytest.data.localManagers.AppMVImageDbManager
import demo.mindvalleytest.data.localManagers.MVImageDbManager

@Module
class LocalDBManagersModule {

    @Provides
    fun provideMVImageDbManager(mvImageDbManager: AppMVImageDbManager): MVImageDbManager =mvImageDbManager
}