package demo.mindvalleytest.data.remote

import demo.mindvalleytest.data.ApiService
import demo.mindvalleytest.data.models.DTO.MVImages
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class AppMVImageApiManager @Inject constructor(private var apiService: ApiService):MVImageApiManager{

    override fun getMvImagesList(): Single<Response<List<MVImages>>> {
        return apiService.getMVImagesList()
    }
}