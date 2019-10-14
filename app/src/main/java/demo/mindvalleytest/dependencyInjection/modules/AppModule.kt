package demo.mindvalleytest.dependencyInjection.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import demo.mindvalleytest.MindValleyTestApplication
import demo.mindvalleytest.data.ApiService
import demo.mindvalleytest.data.AppDatabase
import demo.mindvalleytest.data.local.AppMVImageDbManager
import demo.mindvalleytest.data.local.MVImageDbManager
import demo.mindvalleytest.data.remote.AppMVImageApiManager
import demo.mindvalleytest.data.remote.MVImageApiManager
import demo.mindvalleytest.data.repositories.AppMVImageRepository
import demo.mindvalleytest.data.repositories.MVImageRepository
import demo.mindvalleytest.dependencyInjection.interfaces.ApiUrlInfo
import demo.mindvalleytest.dependencyInjection.interfaces.DatabaseInfo
import demo.mindvalleytest.dependencyInjection.interfaces.DateFormatInfo
import demo.mindvalleytest.utilities.AppConstants
import demo.mindvalleytest.utilities.MVImageDiffCallBacks
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @DatabaseInfo
    fun provideDatabaseName(): String = AppConstants.DB_NAME


    @Provides
    @DateFormatInfo
    fun provideDateFormat(): String =AppConstants.DATE_FORMAT


    @Provides
    @ApiUrlInfo
    fun provideApiURlInfo(): String = AppConstants.API_BASE_URL



    @Provides
    @Singleton
    fun provideContext(application: MindValleyTestApplication): Context = application

    @Provides
    @Singleton
    internal fun provideAppDatabase(@DatabaseInfo dbName: String, context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .fallbackToDestructiveMigration()
            .build()


    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory, @ApiUrlInfo baseUrl: String): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()


    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create<ApiService>(ApiService::class.java)


    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()


    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)


    @Singleton
    @Provides
    fun provideGson(@DateFormatInfo dateFormat: String): Gson = GsonBuilder()
            .setLenient()
            .setDateFormat(dateFormat)
            .create()

    @Provides
    fun provideMVImageDbManager(mvImageDbManager: AppMVImageDbManager):MVImageDbManager=mvImageDbManager

    @Provides
    fun provideMVImageApiManager(mvImageApiManager: AppMVImageApiManager):MVImageApiManager=mvImageApiManager

    @Provides
    fun provideMVImageRepository(mvImageRepository: AppMVImageRepository):MVImageRepository=mvImageRepository

    @Provides
    fun provideMVImageDiffCallBacks(): MVImageDiffCallBacks {
        return MVImageDiffCallBacks()
    }

}