package demo.mindvalleytest.dependencyInjection.builder

import dagger.Module
import dagger.Provides
import demo.mindvalleytest.data.remoteManagers.AppMVImageApiManager
import demo.mindvalleytest.data.remoteManagers.MVImageApiManager

@Module
class RemoteManagersModule {

    @Provides
    fun provideMVImageApiManager(mvImageApiManager: AppMVImageApiManager): MVImageApiManager =
        mvImageApiManager
}