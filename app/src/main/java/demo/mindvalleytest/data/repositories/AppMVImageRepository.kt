package demo.mindvalleytest.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import demo.mindvalleytest.data.local.MVImageDbManager
import demo.mindvalleytest.data.models.DTO.MVImages
import demo.mindvalleytest.data.models.local.MvImagesLocal
import demo.mindvalleytest.data.remote.MVImageApiManager
import demo.mindvalleytest.utilities.NetworkBoundPagedResource
import demo.mindvalleytest.utilities.Resource
import demo.mindvalleytest.utilities.mappers.MvImageModelMapper
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class AppMVImageRepository @Inject constructor(private val mvImageApiManager: MVImageApiManager,private val mvImageDbManager: MVImageDbManager):MVImageRepository {

    override fun getPagedRemotePopularMovieList(page: Int): LiveData<Resource<PagedList<MvImagesLocal>>> {
       return object: NetworkBoundPagedResource<MvImagesLocal, List<MVImages>>() {
           override val pagedListConfiguration: PagedList.Config
               get() = PagedList.Config.Builder()
                   .setPageSize(10)
                   .setEnablePlaceholders(true)
                   .build()

           override fun saveCallResult(item: List<MVImages>) :Completable{
               Log.d("amjad","in save call result ")
               val localItems:ArrayList<MvImagesLocal> =ArrayList<MvImagesLocal>()
               for (mvImage:MVImages in item){
                   localItems.add(MvImageModelMapper.mapRemoteVideoToLocal(mvImage))
               }
               Log.d("amjad","in finished loop now array is size "+localItems.size)
               return mvImageDbManager.insertMvImagesLocalList(localItems)
           }

           override fun loadFromDb(): DataSource.Factory<Int, MvImagesLocal> {
              return mvImageDbManager.getPagedMvImagesLocal()
           }

           override fun createCall(pageNumber: Int): Single<Response<List<MVImages>>> {
             return mvImageApiManager.getMvImagesList()
           }

       }.asLiveData
    }
}