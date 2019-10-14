package demo.mindvalleytest.utilities

import android.os.AsyncTask
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber

abstract class NetworkBoundPagedResource<ResultType, RequestType> @MainThread
constructor() {
    private val result = MediatorLiveData<Resource<PagedList<ResultType>>>()
    private var pageNumber = 1
    private val dbSource: LiveData<PagedList<ResultType>>

    protected abstract val pagedListConfiguration: PagedList.Config

    val asLiveData: LiveData<Resource<PagedList<ResultType>>>
        get() = result

    init {
        result.value = Resource.loading<PagedList<ResultType>>(null)
        val pagedSource = loadFromDb()
        dbSource = buildLivePagedList(pagedSource)
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (this@NetworkBoundPagedResource.shouldFetch(data)) {
                this@NetworkBoundPagedResource.fetchFromNetwork(pageNumber)
            } else {
                result.addSource(
                    dbSource
                ) { newData -> result.setValue(Resource.success(newData)) }
            }
        }
    }

    protected fun buildLivePagedList(pagedSource: DataSource.Factory<Int, ResultType>): LiveData<PagedList<ResultType>> {

        return LivePagedListBuilder(pagedSource, pagedListConfiguration)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<ResultType>() {
                override fun onItemAtEndLoaded(itemAtEnd: ResultType) {
                    super.onItemAtEndLoaded(itemAtEnd)

                    // pageNumber++
                    //fetchFromNetwork(pageNumber)
                }

                override fun onZeroItemsLoaded() {
                    //fetchFromNetwork(dbSource,pageNumber);
                }
            })
            .setInitialLoadKey(1)
            .build()
    }

    private fun fetchFromNetwork(pageNumber: Int) {
        result.removeSource(dbSource)
        createCall(pageNumber).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Response<RequestType>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(response: Response<RequestType>) {
                    if (response.isSuccessful) {
                        saveResultAndReInit(response.body())
                    } else {
                        result.setValue(
                            Resource.error<PagedList<ResultType>>(
                                "unseccuss 403 ",
                                null
                            )
                        )
                    }
                }

                override fun onError(e: Throwable) {
                    Timber.tag("amjadF")
                    Timber.d(e)
                    result.setValue(
                        Resource.error<PagedList<ResultType>>(
                            e.localizedMessage, null
                        )
                    )
                }
            })
    }


    private fun saveResultAndReInit(response: RequestType?) {
        saveCallResult(response!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableCompletableObserver() {
                override fun onComplete() {

                    result.addSource(dbSource) { newData -> result.setValue(Resource.success(newData)) }
                }

                override fun onError(e: Throwable) {
                    result.addSource(dbSource) { result.setValue(Resource.error(e.localizedMessage,null)) }
                }
            })
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType) : Completable

    @MainThread
    open fun shouldFetch(data: PagedList<ResultType>?): Boolean {
        return if (data == null)
            true
        else
            data.size == 0
    }

    @MainThread
    protected abstract fun loadFromDb(): DataSource.Factory<Int, ResultType>

    @MainThread
    protected abstract fun createCall(pageNumber: Int): Single<Response<RequestType>>

    @MainThread
    protected fun onFetchFailed() {
    }
}
