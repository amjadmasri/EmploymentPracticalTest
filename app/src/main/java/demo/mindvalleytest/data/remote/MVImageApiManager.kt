package demo.mindvalleytest.data.remote

import demo.mindvalleytest.data.models.DTO.MVImages
import io.reactivex.Single
import retrofit2.Response

interface MVImageApiManager {
    fun getMvImagesList(): Single<Response<List<MVImages>>>
}