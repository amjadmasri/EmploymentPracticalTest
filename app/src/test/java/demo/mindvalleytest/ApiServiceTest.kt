package demo.mindvalleytest

import demo.mindvalleytest.data.ApiService
import demo.mindvalleytest.data.models.DTO.MVImages
import demo.mindvalleytest.utilities.AppConstants
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import junit.framework.Assert.assertEquals
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ApiServiceUnitTest {

    private var apiService: ApiService? = null


    @Before
    fun createService() {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        apiService = Retrofit.Builder()
            .baseUrl(AppConstants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiService::class.java!!)
    }


    @Test
    fun getMvImagesList() {
        try {
            apiService!!.getMVImagesList().subscribe(object :
                SingleObserver<Response<List<MVImages>>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(response: Response<List<MVImages>>) {
                    assertEquals(response.code().toLong(), 200)

                    var imageList = response.body()

                    assertNotEquals(imageList, null)
                }

                override fun onError(e: Throwable) {

                }
            })


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


}