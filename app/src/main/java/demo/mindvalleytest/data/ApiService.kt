package demo.mindvalleytest.data

import demo.mindvalleytest.data.models.DTO.MVImages
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("raw/wgkJgazE")
    fun getMVImagesList(): Single<Response<List<MVImages>>>
}