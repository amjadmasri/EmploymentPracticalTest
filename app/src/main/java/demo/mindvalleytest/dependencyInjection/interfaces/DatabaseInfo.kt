package demo.mindvalleytest.dependencyInjection.interfaces

import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DatabaseInfo {
}