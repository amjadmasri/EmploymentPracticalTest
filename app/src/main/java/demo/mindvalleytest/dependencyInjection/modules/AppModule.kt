package demo.mindvalleytest.dependencyInjection.modules

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import demo.mindvalleytest.data.ApiService
import demo.mindvalleytest.dependencyInjection.interfaces.ApiUrlInfo
import demo.mindvalleytest.dependencyInjection.interfaces.DatabaseInfo
import demo.mindvalleytest.dependencyInjection.interfaces.DateFormatInfo
import demo.mindvalleytest.utilities.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Provides
    @DatabaseInfo
    fun provideDatabaseName(): String = AppConstants.DB_NAME


    @Provides
    @DateFormatInfo
    fun provideDateFormat(): String =AppConstants.DATE_FORMAT


    @Provides
    @DateFormatInfo
    fun provideApiURlInfo(): String = AppConstants.DATE_FORMAT


    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application


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
    fun provideGson(@DateFormatInfo dateFormat: String): Gson = GsonBuilder()
            .setLenient()
            .setDateFormat(dateFormat)
            .create()


}