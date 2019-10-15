package com.classic.mvloader

import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.classic.mvloader.memoryCacheManagement.MVLoaderCacheManager
import com.classic.mvloader.requestsManagement.NetworkRequestManager
import com.classic.mvloader.utilities.SingletonHolder

/**
 * MVLoader can load an image resource into an image view or a generic resource and getting the ByteArray as a result
 * MVLoader caches images and generic resources efficiently
 * @property context Context
 * @property networkRequestManager NetworkRequestManager
 * @constructor
 */
class MVLoader private constructor(private var context: Context) {
    companion object : SingletonHolder<MVLoader, Context>(::MVLoader)

    private var networkRequestManager: NetworkRequestManager =
        NetworkRequestManager(
            MVLoaderCacheManager()
        )

    init {
        this.context = context.applicationContext
    }

    /**
     * secondary constructor used by the builder
     * @param context Context
     * @param cacheSize Int
     * @constructor
     */
    constructor(context: Context, cacheSize: Int) : this(context) {
        this.networkRequestManager = NetworkRequestManager(
            MVLoaderCacheManager(
                cacheSize
            )
        )
    }

    /**
     * reconfigure the Singleton instance cache size
     * @param cacheSize Int
     * @return MVLoader
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setCacheSize(cacheSize: Int): MVLoader {
        networkRequestManager.cache.resize(cacheSize)
        return this
    }

    /**
     * load a remote image resource into an imageview
     * @param imageView ImageView
     * @param url String
     * @return String?
     */
    fun loadInto(imageView: ImageView, url: String): String? {
        return networkRequestManager.requestImage(imageView, url)
    }

    /**
     * load a remote generic resource (JSON,XML,PDF) and receive a callback with the ByteArray
     * representation of the file,
     * or a failure callback with an appropriate message.
     * @param url String
     * @param loadSuccess Function1<ByteArray, Unit>
     * @param loadFailure Function1<String, Unit>
     * @return requestId String? the request id can be used to cancel the request
     */
    fun loadInto(
        url: String,
        loadSuccess: (ByteArray) -> Unit,
        loadFailure: (String) -> Unit
    ): String? {
        return networkRequestManager.requestResource(url, loadSuccess, loadFailure)
    }

    /**
     * cancel an ongoing loading request with the request ID that is returned by the loadInto methods
     * @param url String
     * @param requestId String
     */
    fun cancelRequest(url: String, requestId: String) {
        networkRequestManager.cancelRequest(url, requestId)
    }

    /**
     * customizable MVLoader builder
     * each call to the builder returns a new Instance and not a singleton instance
     * @property context Context
     * @property cacheSize Int?
     * @constructor
     */
    class Builder(
        private var context: Context
        , private var cacheSize: Int? = null
    ) {
        fun setCacheSize(cacheSize: Int) = apply { this.cacheSize = cacheSize }

        fun build(): MVLoader {
            return MVLoader(context, cacheSize ?: 10)
        }
    }
}