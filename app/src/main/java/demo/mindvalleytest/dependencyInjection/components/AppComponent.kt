package demo.mindvalleytest.dependencyInjection.components

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import demo.mindvalleytest.MindValleyTestApplication
import demo.mindvalleytest.dependencyInjection.builder.ActivityBuilderModule
import demo.mindvalleytest.dependencyInjection.builder.FragmentBuilderModule
import demo.mindvalleytest.dependencyInjection.modules.AppModule
import demo.mindvalleytest.dependencyInjection.modules.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ViewModelModule::class,
    AppModule::class,
    ActivityBuilderModule::class,
    FragmentBuilderModule::class

])
interface AppComponent : AndroidInjector<MindValleyTestApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MindValleyTestApplication): Builder

        fun build(): AppComponent
    }

    override fun inject(app: MindValleyTestApplication)
}